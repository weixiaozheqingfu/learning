package com.glitter.spring.boot.web.action.oauth;

import com.glitter.spring.boot.bean.*;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.*;
import com.glitter.spring.boot.web.action.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/sso")
public class OauthAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Autowired
    private IOauthCodeService oauthCodeService;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    @Autowired
    private IAccessToken4AuthorizationCodeService accessToken4AuthorizationCodeService;

    @Autowired
    private IAccessToken4RefreshTokenService accessToken4RefreshTokenService;

    /**
     * 该方法为客户端应用访问，客户端应用访问该方法时应以重定向方式访问（前端重定向或后端重定向均可，将浏览器资源执行授权中心的授权资源页面，即本地址）
     * 示例: http://localhost:8080/oauth2/authorize?client_id=1001&redirect_uri=http://localhost:8081/auth/sso/callback&state=sdf8gswer9t89fdb8s9fg8r
     */
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String authorize(Model model,
                            HttpServletResponse httpServletResponse,
                            @RequestParam(required = false) String client_id,
                            @RequestParam(required = false) String redirect_uri,
                            @RequestParam(required = false) String scope,
                            @RequestParam(required = false) String response_type,
                            @RequestParam(required = false) String state) {
        if (StringUtils.isBlank(client_id)) {
            model.addAttribute("errorMsg", "clientId参数错误");
            return "/error";
        }
        if (StringUtils.isBlank(redirect_uri)) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if (null == oauthClientInfo) {
            model.addAttribute("errorMsg", "clientId参数错误");
            return "/error";
        }
        String redirect_uri_decode;
        try {
            redirect_uri_decode = URLDecoder.decode(redirect_uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        if (!oauthClientInfo.getRedirectUri().equals(redirect_uri) && !oauthClientInfo.getRedirectUri().equals(redirect_uri_decode)) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        if (StringUtils.isBlank(response_type)) {
            response_type = GlitterConstants.OAUTH_RESPONSE_TYPE_CODE;
        }
        if (StringUtils.isBlank(scope)) {
            scope = "get_user_open_info";
        }

        // 检查用户是否已处于登陆状态
        UserInfo userInfo = (UserInfo) sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
        logger.info("authorize方法sessionHandler.getSession().getId():" + sessionHandler.getSession().getId());
        if (null == userInfo) {
            // 如果用户未登录，则返回登录页面。本版本是forward方式，采用重定向方式也可以，只要到达授权中心的授权页面即可。
            model.addAttribute("client_id", client_id);
            model.addAttribute("redirect_uri", redirect_uri);
            model.addAttribute("scope", scope);
            model.addAttribute("response_type", response_type);
            model.addAttribute("state", state);
            return "/authorize";
        } else {
            // 如果用户已登录，则生成code码回调地址，并使浏览器重定向到该回调地址。
            String callbackUrl = this.generateCodeUrl(sessionHandler.getSession().getId(), client_id, userInfo.getId(), scope, oauthClientInfo.getRedirectUri(), state);
            try {
                httpServletResponse.sendRedirect(callbackUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 预授权方法:如果是ajax请求，该方法不可跨域访问
     * 该方法为授权中心登录页对应方法，被授权中心登录页调用，不对外提供。
     * 方法名调整为由authorize调整为login更合适。因为这里是sso单点登录功能，而不是标准的oauth授权，如果是oauth授权，方法名字命名为authorize比较好，命名为login就狭隘了，但现在是sso，login更合适。
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseResult<String> login(Model model,
                                        @RequestParam(required = false) String client_id,
                                        @RequestParam(required = false) String redirect_uri,
                                        @RequestParam(required = false) String scope,
                                        @RequestParam(required = false) String response_type,
                                        @RequestParam(required = false) String state,
                                        @RequestParam(required = false) String u,
                                        @RequestParam(required = false) String p) {
        // 1.验证auth参数最基本的合法性
        if (StringUtils.isBlank(client_id)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (StringUtils.isBlank(redirect_uri)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if (null == oauthClientInfo) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        String redirect_uri_decode;
        try {
            redirect_uri_decode = URLDecoder.decode(redirect_uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (!oauthClientInfo.getRedirectUri().equals(redirect_uri) && !oauthClientInfo.getRedirectUri().equals(redirect_uri_decode)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (StringUtils.isBlank(response_type)) {
            response_type = GlitterConstants.OAUTH_RESPONSE_TYPE_CODE;
        }
        if (StringUtils.isBlank(scope)) {
            scope = "get_user_open_info";
        }
        if (!scope.equals("get_user_open_info")) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (StringUtils.isBlank(u) && StringUtils.isBlank(p)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "用户名或密码为空");
        }

        // 2.验证用户身份
        UserInfo userInfo = userInfoService.getUserInfoByAccount(u);
        if (null == userInfo || !userInfo.getPassword().equals(p)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "用户名或密码错误");
        }

        // 3.创建sso认证中心的全局登录会话
        sessionHandler.getSession().setAttribute(GlitterConstants.SESSION_USER, userInfo);

        // 4.生成code码回调地址
        String callbackUrl = this.generateCodeUrl(sessionHandler.getSession().getId(), client_id, userInfo.getId(), scope, oauthClientInfo.getRedirectUri(), state);
        return ResponseResult.success(callbackUrl);
    }

    private String generateCodeUrl(String jsessionid, String client_id, Long userId, String scope, String redirectUri, String state) {
        // 1. 生成code码。
        OauthCode oauthCode = new OauthCode();
        oauthCode.setJsessionid(jsessionid);
        oauthCode.setClientId(client_id);
        oauthCode.setUserId(userId);
        oauthCode.setScope(scope);
        // InterfaceUri与scope的对应关系应该有一张数据库表对应关系，sso需求比较固定固定，此处简化，直接代码写死。
        oauthCode.setInterfaceUri("/sso/resource/userinfo,/sso/resource/keepAlive,/sso/resource/logout");
        String code = oauthCodeService.generateCode(oauthCode);

        // 2.组装回调地址
        String callbackUrl;
        try {
            String redirectUriDecode = URLDecoder.decode(redirectUri, "UTF-8");
            if (redirectUriDecode.indexOf("?") > 0) {
                callbackUrl = redirectUriDecode + "&code=" + code + "&state=" + state;
            } else {
                callbackUrl = redirectUriDecode + "?code=" + code + "&state=" + state;
            }
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        return callbackUrl;
    }

    /**
     * 请求示例:
     * http://localhost:8080/oauth2/access_token?client_id=1001&client_secret=123456&redirect_uri=https://gitee.com/auth/csdn/callback&code=sdf8gsweb8s9fg8r&grant_type=authorization_code
     * <p>
     * 正确返回样例：
     * {
     * "access_token":"ACCESS_TOKEN",
     * "token_type":"bearer",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "scope":"SCOPE",
     * "openid":"OPENID"
     * }
     * <p>
     * 错误返回样例：
     * {
     * "errcode":40029,
     * "errmsg":"invalid code"
     * }
     */
    @ResponseBody
    @RequestMapping(value = "access_token", method = RequestMethod.GET)
    public ResponseResult<Map<String, Object>> getAccessToken(@RequestParam(required = false) String client_id,
                                              @RequestParam(required = false) String client_secret,
                                              @RequestParam(required = false) String redirect_uri,
                                              @RequestParam(required = false) String code,
                                              @RequestParam(required = false) String grant_type) {
        Map resultMap = new HashMap<>();
        try {
            // grant_type的不同会有不同的实现类,这里演示的是grant_type=authorization_code授权码模式根据code换取accessToken的情况.
            if (GlitterConstants.OAUTH_GRANT_TYPE_AUTHORIZATION_CODE.equals(grant_type)) {
                // 1.验证客户端身份和code信息获取accessToken
                AccessTokenOutParam accessTokenInfo = accessToken4AuthorizationCodeService.getAccessTokenInfo(client_id, client_secret, redirect_uri, code, grant_type);
                if (null == accessTokenInfo) {
                    return ResponseResult.fail(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
                }
                logger.info("getAccessToken方法accessTokenInfo.getJsessionid():" + accessTokenInfo.getJsessionid());

                resultMap.put("access_token", accessTokenInfo.getAccess_token());
                resultMap.put("token_type", accessTokenInfo.getToken_type());
                resultMap.put("expires_in", accessTokenInfo.getExpires_in());
                resultMap.put("refresh_token", accessTokenInfo.getRefresh_token());
                resultMap.put("scope", accessTokenInfo.getScope());
                resultMap.put("userid", accessTokenInfo.getUserId());

                // 2.验证accessToken所依附的jessionid全局会话是否仍在会话期间内
                oauthAccessTokenService.validateJsessionid(accessTokenInfo.getJsessionid());

                // 3.为全局会话续期
                sessionHandler.renewal(accessTokenInfo.getJsessionid());

                // 4.sso中,换取accessToken成功,即认为客户端应用创建其局部会话成功,作局部会话与全局会话通信的凭证,一根绳上的蚂蚱。

            } else if (GlitterConstants.OAUTH_GRANT_TYPE_PASSWORD.equals(grant_type)) {
                // ......
            } else {
                return ResponseResult.fail(CoreConstants.REQUEST_ERROR_PARAMS, "grant_type参数值非法");
            }
            return ResponseResult.success(resultMap);
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

    /**
     * 检验授权凭证（access_token）是否有效
     * <p>
     * 其实client_id参数和openid参数可以不传
     * 因为只是一个有效性验证而已,传不传都行,传的话,相对更严格,但也只是掩耳盗铃,因为如果access_token被泄露了,client_id和openid通常也不保,所以不传也没问题,传了就给点安慰.
     * 因为获取access_token的过程是https通讯,只有合法的客户端才会持有access_token,除非客户端主动泄露access_token或者没有保护好被攻击了,那都是属于客户端自己的安全范畴了
     *
     * <p>
     * 正确返回样例：
     * {
     * "code":0,
     * "msg":"ok",
     * "remaining_expiration_time":"3600"
     * }
     * <p>
     * 错误返回样例：
     * {
     * "errcode":50036,
     * "errmsg":"invalid access_token"
     * }
     *
     * 特殊说明,客户端需要对不通的返回码做出对应的响应。
     *
     * "0": "accessToken有效",客户端继续其业务流程。
     * "50034": "accessToken输入参数为空", 客户端传参问题,客户端自行解决。
     * "50035": "accessToken失效",客户端考虑使用refreshToken重新换取accessToken,如果refreshToken换取失败,比如refreshToken也失效了, 则可以考虑注销本地会话并重新跳转到sso授权登录页,
     *          此时如果sso全局会话依然是存在的,那么客户端就会完成重新自动登录的过程,重新建立和sso用户中心的关系,拿到新的对应的accessToken,并关联建立新的局部会话,整个过程用户无感。
     *          如果sso全局会话是不存在的,则该客户端即第一个完成sso全局登录的客户端,这个过程只是多了用户输入用户名和密码的过程,后续流程依然是重新建立和sso用户中心的关系,拿到新的对应的accessToken,并关联建立新的局部会话。
     * "60032": 表示全局会话已失效,accessToken自然也失效,此时客户端应该注销自己的局部会话,并重新跳转到sso授权登录页,后续过程同50035描述。
     * xx即其他返回码: 应该是sso中心其他系统异常情况,此时客户端应该注销自己的局部会话,并重新跳转到sso授权登录页,后续过程同50035描述。
     *                既然sso中心出问题了,那客户端自己也不能局部会话偷偷登录着,必须退出局部会话，并重试sso登录,能登录上应该一切自然恢复,不能登录,则局部会话已经退出,没有安全问题。
     *                并且客户端应该线下将情况反馈到sso中心的同事排查问题，当然sso中心自己也应该有报警，并排查问题。
     *
     * @param access_token
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public ResponseResult<Map<String, Object>> auth(@RequestParam(required = false) String access_token) {
        Map resultMap = new HashMap<>();
        try {
            // 1.验证access_token是否有效
            AccessTokenInParam accessTokenInParam = oauthAccessTokenService.validateAccessToken(access_token);

            // 2.为全局会话续期
            sessionHandler.renewal(accessTokenInParam.getJsessionid());

            resultMap.put("code", "0");
            resultMap.put("msg", "ok");
            resultMap.put("remaining_expiration_time", accessTokenInParam.getRemainingExpirationTime());
            return ResponseResult.success(resultMap);
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

    /**
     * @param client_id
     * @param refresh_token
     * @param grant_type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refresh_token", method = RequestMethod.GET)
    public ResponseResult<Map<String, Object>> refresh_token(@RequestParam(required = false) String client_id,
                                             @RequestParam(required = false) String refresh_token,
                                             @RequestParam(required = false) String grant_type) {
        Map resultMap = new HashMap<>();
        try {
            if (!GlitterConstants.OAUTH_GRANT_TYPE_REFRESH_TOKEN.equals(grant_type)) {
                return ResponseResult.fail(CoreConstants.REQUEST_ERROR_PARAMS, "grant_type参数非法");
            }
            // 1.获取refresh_token对应的条目
            AccessTokenOutParam accessTokenOutParam = accessToken4RefreshTokenService.getAccessTokenInfo(client_id, refresh_token, grant_type);
            if (null == accessTokenOutParam) {
                return ResponseResult.fail(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            }

            // 2.验证accessToken所依附的jessionid全局会话是否仍在会话期间内
            oauthAccessTokenService.validateJsessionid(accessTokenOutParam.getJsessionid());

            // 3.为全局会话续期
            sessionHandler.renewal(accessTokenOutParam.getJsessionid());

            resultMap.put("access_token", accessTokenOutParam.getAccess_token());
            resultMap.put("token_type", accessTokenOutParam.getToken_type());
            resultMap.put("expires_in", accessTokenOutParam.getExpires_in());
            resultMap.put("refresh_token", accessTokenOutParam.getRefresh_token());
            resultMap.put("scope", accessTokenOutParam.getScope());
            resultMap.put("userid", accessTokenOutParam.getUserId());

            return ResponseResult.success(resultMap);
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            return ResponseResult.fail(ex.getCode(), ex.getMessage());
        }
    }

}