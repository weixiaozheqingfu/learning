package com.glitter.spring.boot.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.persistence.remote.ISsoRemote;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.service.ISessionHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ISessionHandler sessionHandler;
    @Autowired
    ICommonCache commonCache;
    @Autowired
    ICacheKeyManager cacheKeyManager;
    @Autowired
    ISsoRemote ssoRemote;
    @Autowired
    IOauthAccessTokenService oauthAccessTokenService;
    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String loginUrl = GlitterConstants.DOMAIN_SSO_CLIETN1 +"/login";

        String jsessionIdCookie = JsessionIdCookieContext.get();
        if (StringUtils.isBlank(jsessionIdCookie)) {
            this.response(httpServletRequest, httpServletResponse, loginUrl,"-2", "用户未登陆");
            return false;
        }

        ISession session = sessionHandler.getSession();
        if (!session.getId().equals(jsessionIdCookie)) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "未登录或会话超时，请重新登陆。");
            return false;
        }

        UserInfo userInfo;
        if (null == (userInfo = (UserInfo) session.getAttribute(GlitterConstants.SESSION_USER))) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "用户未登陆");
            return false;
        }

        // 登录状态jsessionIdCookie会话维护了一个accessToken与全局会话保持通讯
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        if (oauthAccessTokenDb == null || StringUtils.isBlank(oauthAccessTokenDb.getAccessToken())) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "系统异常");
            return false;
        }

        // 校验单端登录
        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getUserId())));
        if (StringUtils.isBlank(jsessionIdEffective)) {
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "未登录或会话超时，请重新登陆。");
            return false;
        }
        if (!jsessionIdCookie.equals(jsessionIdEffective)) {
            // 被其他端"挤掉"了,注销局部会话。
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            // 注销全局会话。因为如果不注销这端的单点登录整体会话,那么用户收到下面的提示后，点击这段任何客户端的任何功能都会在后台再次默默完成登录，再把单端会话抢过来。
            // 这体验就不太好了，相当于强制把另一端的登录给踢掉了，而这个操作应该留给用户来做才好。
            // 所以既然登录被其他端踢掉了，那咱这端就整体注销，如果用户要这端登录，那就自行在这端完成登录挤掉那一端整体会话即可。
            ssoRemote.logout(oauthAccessTokenDb.getAccessToken());
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-2", "您的账号已在其它地方登陆，若不是本人操作，请注意账号安全！");
            return false;
        }

        // 客户端登录校验通过的情况下,需要验证accessToken是否有效,目的有二:
        // 1.当前accessToken如果无效,需要及时换取新的accessToken,以保持客户端和sso认证中心之间通信的畅通。
        // 2.accessToken其实代表了sso全局会话,因为是一根绳,通过accessToken可以窥探全局会话是否失效,如果accessToken对应的sso全局会话失效,那么就要注销本地会话,走重新登录流程。
        try {
            this.authAccessToken(oauthAccessTokenDb.getAccessToken());
        } catch (Exception e) {
            // 注销本地会话,走重新登录流程,走重新登录流程有两种情况
            // (为什么sso全局会话可能会是失效呢,因为用户可能先通过其他客户端点击退出登录操作了,此时全局会话已经失效,然后再切换到当前客户端页面触发资源请求操作,即本次代码流程)
            // 1.sso全局会话有效,则重新登录流程不需要用户参与,后台默默走->code->accessToken->userInfo->局部会话的流程,然后跳转到系统首页。这根绳上换新的access_token和jsessionid_client这两个蚂蚱。
            // 2.sso全局会话失效,则重新登录流程需要用户输入用户名和密码,然后走->code->accessToken->userInfo->局部会话的流程,然后跳转到系统首页。这是一个全新的sso全局会话整体,绳和蚂蚱都是新的。
            oauthAccessTokenService.deleteByJsessionid(session.getId());
            session.invalidate();
            // 如果是ajax请求,当前端收到返回码是-3时,则直接跳转到loginUrl,不再进行弹窗提示,这是最友好的。
            // 如果非ajax请求,则也是直接跳转到loginUrl,不再进行弹窗提示,这是最友好的。
            this.response(httpServletRequest, httpServletResponse, loginUrl, "-3", "系统异常,请重试");
            return false;
        }

        // 不管accessToken本来就是有效的,还是通过refreshToken换取了最新的accessToken,代码运行到此处都能保证当前的库中的accessToken是有效的,sso全局会话这一根绳是串起来的,sso全局整体会话是有效的。
        // 由于上述流程已经为客户端局部会话完成了续期,现在对sso全局会话续期。
        OauthAccessToken oauthAccessTokenDb2 = oauthAccessTokenService.getOauthAccessTokenByJsessionid(jsessionIdCookie);
        ssoRemote.keepAlive(oauthAccessTokenDb2.getAccessToken());

        logger.info("LoginInterceptor.preHandle验证成功,jsessionId:{},userId:{},fullName:{}", jsessionIdCookie, userInfo.getUserId(), userInfo.getNickName());
        return true;
    }

    /**
     * 校验accessToken有效性
     * 如果有效则罢了
     * 如果无效{
     *     则触发refreshToken换取accessToken,最终得到一个新的有效的aceessToken,维持一根绳
     *     {
     *         如果这个过程顺利则罢了
     *         如果这个过程失败或者异常,则抛出异常,由调用方注销局部会话,并且走重新登录流程.
     *
     *         特注:一般情况是refresh_token也失效,导致其换取access_token失败,不管什么原因导致换取失败,此时都要抛出异常,由调用方注销局部会话,并且走重新登录流程.
     *              因为不注销也不行了,这根绳已经断了,不注销后续没有办法与sso用户中心通讯了,比如续期等操作都没办法做,只能注销通过重新登录重新把这根绳建立起来。
     *     }
     * }
     * 如果accessToken对应的sso全局会话已经失效,sso全局会话整体的前提已失,accessToken没有意义,局部会话没有意义,一根线已经断开无法维系。此时当则抛出异常,由调用方注销局部会话,并且走重新登录流程.
     * 如果是其他异常情况,一般是sso_server系统报错。这证明当前这跟线不稳定,sso全局会话整体的前提已失,accessToken没有意义,局部会话没有意义,一根线已经断开无法维系。则抛出异常,由调用方注销局部会话,并且走重新登录流程.
     *
     * 这个方法本身也可能抛出运行时异常,这证明当前客户端代码是不稳定的,需要记录异常,并排查解决问题。一旦有运行时异常,为了保证系统运行安全,也是抛出异常,由调用方注销局部会话,并且走重新登录流程.当然这个异常是系统自动抛出的。
     *
     * @param accessToken
     */
    private void authAccessToken(String accessToken) throws Exception{
        try {
            // 客户端登录校验通过的情况下,需要验证accessToken是否有效,目的有二:
            // 1.当前accessToken如果无效,需要及时换取新的accessToken,以保持客户端和sso认证中心之间通信的畅通。
            // 2.accessToken其实代表了sso全局会话,因为是一根绳,通过accessToken可以窥探全局会话是否失效,如果accessToken对应的sso全局会话失效,那么就要注销本地会话，跳转到登录页。
            ResponseResult<Map> responseResult = ssoRemote.auth(accessToken);
            logger.info(JSONObject.toJSONString(responseResult));

            // 如果accessToken有效,则考虑其有效期剩余小于30秒的问题。
            if (responseResult.getCode().equals("0")) {
                // 取出remaining_expiration_time。
                Map<String, String> map = responseResult.getData();
                Object remainingExpirationTimeObj = map == null ? null : map.get("remaining_expiration_time");
                String remainingExpirationTimeStr = remainingExpirationTimeObj == null ? null : String.valueOf(remainingExpirationTimeObj);
                Long remainingExpirationTimeLong = remainingExpirationTimeStr == null ? null : Long.valueOf(remainingExpirationTimeStr);
                if (null == map || null == remainingExpirationTimeLong) {
                    throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
                }

                // 如果accessToken有效期剩余小于30秒,也认为过期,触发refresh_token换access_token过程。
                // 有效期剩余小于30秒的accessToken是不安全的,继续使用它通信可能随时面临失效而引发失败的风险,所以也当作已过期处理。
                if (remainingExpirationTimeLong < 30L ) {
                    try {
                        this.refreshToken(accessToken);
                    } catch (Exception e) {
                        // 一般情况是refresh_token也失效,导致其换取access_token失败,不管什么原因导致换取失败,此时都要抛出异常,由调用方注销局部会话,并且走重新登录流程.
                        // 因为不注销也不行了,这根绳已经断了,不注销后续没有办法与sso用户中心通讯了,比如续期等操作都没办法做,只能注销通过重新登录重新把这根绳建立起来。
                        logger.info(JSONObject.toJSONString(e));
                        throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "refreshToken换取accessToken失败");
                    }
                }
            // 如果accessToken失效,触发refresh_token换access_token过程。
            } else if (responseResult.getCode().equals("50035")) {
                try {
                    this.refreshToken(accessToken);
                } catch (Exception e) {
                    // 一般情况是refresh_token也失效,导致其换取access_token失败,不管什么原因导致换取失败,此时都要抛出异常,由调用方注销局部会话,并且走重新登录流程.
                    // 因为不注销也不行了,这根绳已经断了,不注销后续没有办法与sso用户中心通讯了,比如续期等操作都没办法做,只能注销通过重新登录重新把这根绳建立起来。
                    logger.info(JSONObject.toJSONString(e));
                    throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "refreshToken换取accessToken失败");
                }
            // 如果accessToken对应的sso全局会话失效,抛出异常,统一处理,即注销本地局部会话,走重新登录流程。
            } else if (responseResult.getCode().equals("60032")) {
                throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "sso全局会话失效");
            // 其他异常情况,抛出异常,统一处理,即注销本地局部会话,走重新登录流程。
            } else {
                throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "sso全局会话通信异常");
            }
        } catch (Exception e) {
            logger.info(JSONObject.toJSONString(e));
            throw e;
        }
    }

    /**
     * refreshToken刷accessToken
     * @param refreshToken
     * @throws Exception
     */
    private void refreshToken(String refreshToken) throws Exception {
        try {
            // 使用refreshToken刷accessToken
            OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByServerType("sso");
            String client_id = oauthClientInfo.getClientId();

            Map refreshTokenMap = ssoRemote.refreshToken(client_id, refreshToken, "refresh_token");
            String access_token = null == refreshTokenMap ? null :(String)refreshTokenMap.get("access_token");
            String expires_in = null == refreshTokenMap ? null : (String)refreshTokenMap.get("expires_in");
            String refresh_token = null == refreshTokenMap ? null : (String)refreshTokenMap.get("refresh_token");
//          String token_type = null == refreshTokenMap ? null : (String)refreshTokenMap.get("token_type");
//          String scope = null == refreshTokenMap ? null : (String)refreshTokenMap.get("scope");
//          Long userId = null == refreshTokenMap ? null : Long.valueOf((String)refreshTokenMap.get("userId"));

            // 其他字段这里偷个懒不一一判断了
            if (null == refreshTokenMap || StringUtils.isBlank(access_token)) {
                throw new BusinessException("-1", "refreshToken换取accessToken失败");
            }

            // 更新accessToken信息
            OauthAccessToken oauthAccessToken = new OauthAccessToken();
            oauthAccessToken.setAccessToken(access_token);
            oauthAccessToken.setExpireIn(StringUtils.isBlank(expires_in) ? null : Long.valueOf(expires_in));
            oauthAccessToken.setRefreshToken(refresh_token);
            oauthAccessTokenService.modifyById(oauthAccessToken);
        } catch (Exception e) {
            logger.info(JSONObject.toJSONString(e));
            throw e;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        ContextManager.removeAllContext();
    }

    /**
     * 是否是ajax请求
     *
     * @param request
     * @return
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        boolean flag1 = accept != null && accept.indexOf("application/json") != -1;
        boolean flag2 = xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1;
        boolean isAjax = flag1 || flag2;
        return isAjax;
    }

    /**
     * 返回响应数据
     * <p>
     * 测试的话可以是postman模拟ajax,只要在postman的header头中加入X-Requested-With属性,值为XMLHttpRequest即可.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param code
     * @param msg
     * @return
     * @throws IOException
     */
    private void response(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String loginUrl, String code, String msg) throws IOException {
        if (isAjaxRequest(httpServletRequest)) {
            httpServletRequest.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("text/html;charset=utf-8");
            ResponseResult result = new ResponseResult(code, msg);
            result.setData(loginUrl);
            httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
            return;
        }
        httpServletResponse.sendRedirect(loginUrl);
        return;
    }

}
