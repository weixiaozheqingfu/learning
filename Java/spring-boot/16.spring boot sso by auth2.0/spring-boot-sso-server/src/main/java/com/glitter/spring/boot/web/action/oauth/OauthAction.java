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
@RequestMapping("/oauth2")
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
        oauthCode.setInterfaceUri("/oauth2/userinfo,/oauth2/logout");
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
    public Map<String, Object> getAccessToken(@RequestParam(required = false) String client_id,
                                              @RequestParam(required = false) String client_secret,
                                              @RequestParam(required = false) String redirect_uri,
                                              @RequestParam(required = false) String code,
                                              @RequestParam(required = false) String grant_type) {
        Map resultMap = new HashMap<>();
        Map errorMap = new HashMap<>();
        try {
            // grant_type的不同会有不同的实现类,这里演示的是grant_type=authorization_code授权码模式根据code换取accessToken的情况.
            if (GlitterConstants.OAUTH_GRANT_TYPE_AUTHORIZATION_CODE.equals(grant_type)) {
                // 1.验证客户端身份和code信息accessToken
                AccessTokenOutParam accessTokenInfo = accessToken4AuthorizationCodeService.getAccessTokenInfo(client_id, client_secret, redirect_uri, code, grant_type);
                if (null == accessTokenInfo) {
                    errorMap.put("errcode", CoreConstants.REQUEST_PROGRAM_ERROR_CODE);
                    errorMap.put("errmsg", "系统异常");
                    return errorMap;
                }
                resultMap.put("access_token", accessTokenInfo.getAccess_token());
                resultMap.put("token_type", accessTokenInfo.getToken_type());
                resultMap.put("expires_in", accessTokenInfo.getExpires_in());
                resultMap.put("refresh_token", accessTokenInfo.getRefresh_token());
                resultMap.put("scope", accessTokenInfo.getScope());
                resultMap.put("userid", accessTokenInfo.getUserId());

                // 2.sso中,换取accessToken成功,即认为客户端应用创建子会话成功,作子会话标记,方便将来注销会话时回调。

            } else if (GlitterConstants.OAUTH_GRANT_TYPE_PASSWORD.equals(grant_type)) {
                // ......
            } else {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "grant_type参数值非法");
                return errorMap;
            }
            return resultMap;
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            errorMap.put("errcode", ex.getCode());
            errorMap.put("errmsg", ex.getMessage());
            return errorMap;
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
     * "errcode":0,
     * "errmsg":"ok"
     * }
     * <p>
     * 错误返回样例：
     * {
     * "errcode":40003,
     * "errmsg":"invalid openid"
     * }
     *
     * @param access_token
     * @param openid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public Map<String, Object> auth(@RequestParam(required = false) String client_id,
                                    @RequestParam(required = false) String access_token,
                                    @RequestParam(required = false) String openid) {
        Map resultMap = new HashMap<>();
        Map errorMap = new HashMap<>();
        try {
            if (StringUtils.isBlank(client_id)) {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "client_id参数为空");
                return errorMap;
            }
            if (StringUtils.isBlank(access_token)) {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "access_token参数为空");
                return errorMap;
            }
            if (StringUtils.isBlank(openid)) {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "openid参数为空");
                return errorMap;
            }
            OauthAccessToken oauthAccessToken = oauthAccessTokenService.getOauthAccessToken(access_token);
            if (null == oauthAccessToken) {
                errorMap.put("errcode", "600031");
                errorMap.put("errmsg", "access_token invalid");
                return errorMap;
            }
            if (!client_id.equals(oauthAccessToken.getClientId())) {
                errorMap.put("errcode", "600032");
                errorMap.put("errmsg", "access_token invalid");
                return errorMap;
            }
            if (System.currentTimeMillis() > oauthAccessToken.getRefreshTokenExpireTime().getTime()) {
                errorMap.put("errcode", "600034");
                errorMap.put("errmsg", "access_token已过期");
                return errorMap;
            }
            errorMap.put("errcode", "0");
            errorMap.put("errmsg", "ok");
            return resultMap;
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            errorMap.put("errcode", ex.getCode());
            errorMap.put("errmsg", ex.getMessage());
            return errorMap;
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
    public Map<String, Object> refresh_token(@RequestParam(required = false) String client_id,
                                             @RequestParam(required = false) String refresh_token,
                                             @RequestParam(required = false) String grant_type) {
        Map resultMap = new HashMap<>();
        Map errorMap = new HashMap<>();
        try {
            if (!GlitterConstants.OAUTH_GRANT_TYPE_REFRESH_TOKEN.equals(grant_type)) {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "grant_type参数非法");
                return errorMap;
            }

            AccessTokenOutParam accessTokenInfo = accessToken4RefreshTokenService.getAccessTokenInfo(client_id, refresh_token, grant_type);
            if (null == accessTokenInfo) {
                errorMap.put("errcode", CoreConstants.REQUEST_PROGRAM_ERROR_CODE);
                errorMap.put("errmsg", "系统异常");
                return errorMap;
            }
            resultMap.put("access_token", accessTokenInfo.getAccess_token());
            resultMap.put("token_type", accessTokenInfo.getToken_type());
            resultMap.put("expires_in", accessTokenInfo.getExpires_in());
            resultMap.put("refresh_token", accessTokenInfo.getRefresh_token());
            resultMap.put("scope", accessTokenInfo.getScope());
            resultMap.put("userid", accessTokenInfo.getUserId());

            return resultMap;
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            errorMap.put("errcode", ex.getCode());
            errorMap.put("errmsg", ex.getMessage());
            return errorMap;
        }
    }

}