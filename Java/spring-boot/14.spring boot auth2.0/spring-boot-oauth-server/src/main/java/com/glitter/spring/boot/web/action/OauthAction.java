package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.*;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/oauth2")
public class OauthAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthAction.class);

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthCodeService oauthCodeService;

    @Autowired
    private IOauthScopeEnumService oauthScopeEnumService;

    @Autowired
    private IAccessTokenService accessTokenService;

    /**
       示例: http://localhost:8080/oauth2/authorize?client_id=1001&redirect_uri=https://gitee.com/auth/csdn/callback&state=sdf8gswer9t89fdb8s9fg8r
     */
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String authorize(Model model,
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
        if (StringUtils.isBlank(scope)) {
            scope = "get_user_open_info";
        } else {
            List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumService.getAll();
            List<String> scopeNames = oauthScopeEnums.stream().map(OauthScopeEnum::getScopeName).distinct().collect(Collectors.toList());
            List<String> scopes = Arrays.asList(scope.split(","));
            if (!scopeNames.containsAll(scopes)) {
                model.addAttribute("errorMsg", "scope参数错误");
                return "/error";
            }
        }
        if (StringUtils.isBlank(response_type)) {
            response_type = "code";
        }

        // 准备scopes数据
        List<OauthScopeEnum> scopes = oauthScopeEnumService.getByScopeNames(Arrays.asList(scope.split(",")));
        model.addAttribute("scopes", scopes);

        // 检查用户是否已处于登陆状态
        UserInfo userInfo = (UserInfo)sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
        model.addAttribute("userId", null == userInfo ? null : userInfo.getId());
        model.addAttribute("fullName", null == userInfo ? null : userInfo.getFullName());

        model.addAttribute("client_id", client_id);
        model.addAttribute("redirect_uri", redirect_uri);
        model.addAttribute("scope", scope);
        model.addAttribute("response_type", response_type);
        model.addAttribute("state", state);
        return "/authorize";
    }


    /**
     * 预授权方法:该方法不可跨域访问
     * <p>
     * 验证用户名密码后，有几种处理方案，
     * 1.该授权登录和服务器正常登陆域名一致，cookie写入的域名和服务器端的session对象生成完全用一套，授权后，也相当于登陆了该服务器系统，会往客户端写cookie。
     * 这样下次认证时，如果已经登陆过了且没有过期，可以不用再次输入用户名和密码。
     * 2.该授权登录和服务器正常登陆域名分开，cookie写入的域名和服务器端的session对象生成各自用一套，授权后，只是登陆了该授权系统，会往客户端对应的授权域名写cookie。
     * 这样下次认证时，如果已经在授权服务器登陆过了且没有过期，可以不用再次输入用户名和密码。
     * 3.不论是上面的方案1还是方案2,都专门为授权做一次单独的用户名密码的认证,没有任何cookie和服务器会话对象相关内容,做纯粹的授权时候使用的用户身份信息验证。
     * <p>
     * 建议使用第二种,通常情况下,这里是没有图形验证码的,与正常登陆混在一块不太安全,而如果不进行登陆记录用户状态,用户重复登陆时,还需要再次输入用户名密码
     * 这种一下,就采用第二种方案了,并且通常情况这些大站点都是采用第二种方式,比如qq,csdn等,他们都有对外开发api的单独域名,当然也有采用第一种方式的,比如github.
     * 本地模拟由于模拟多域名比较麻烦,所以就采用第一种方式。
     *
     * @param model
     * @param client_id
     * @param redirect_uri
     * @param scope
     * @param response_type
     * @param state
     * @param u
     * @param p
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "authorize", method = RequestMethod.POST)
    public ResponseResult<String> authorize(Model model,
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
        if (StringUtils.isBlank(scope)) {
            scope = "get_user_open_info";
        } else {
            List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumService.getAll();
            List<String> scopeNames = oauthScopeEnums.stream().map(OauthScopeEnum::getScopeName).distinct().collect(Collectors.toList());
            List<String> scopes = Arrays.asList(scope.split(","));
            if (!scopeNames.containsAll(scopes)) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
            }
        }
        if (StringUtils.isBlank(response_type)) {
            response_type = "code";
        }

        // 2.验证用户身份
        UserInfo userInfo;
        if (StringUtils.isNotBlank(u) && StringUtils.isNotBlank(p)) {
            userInfo = userInfoService.getUserInfoByAccount(u);
            if (null == userInfo || !userInfo.getPassword().equals(p)) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "用户名或密码错误");
            }
            sessionHandler.getSession().setAttribute(GlitterConstants.SESSION_USER, userInfo);
        } else {
            userInfo = (UserInfo) sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
            if (null == userInfo) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "会话已过期,请重新登陆并授权");
            }
        }

        // 3.生成code码
        String code = "";
        // 目前就支持code模式
        if(response_type.equals("code")){
            OauthCode oauthCode = new OauthCode();
            oauthCode.setUserId(userInfo.getId());
            oauthCode.setClientId(client_id);
            oauthCode.setScope(scope);
            code = oauthCodeService.generateCode(oauthCode);
        }

        // 4.生成回调地址
        String callbackUrl;
        try {
            String redirectUriDecode = URLDecoder.decode(oauthClientInfo.getRedirectUri(), "UTF-8");
            if(redirectUriDecode.indexOf("?") > 0){
                callbackUrl =  redirectUriDecode + "&code=" + code + "&state=" + state;
            }else{
                callbackUrl =  redirectUriDecode + "?code=" + code + "&state=" + state;
            }
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        return ResponseResult.success(callbackUrl);
    }


    /**
       请求示例:
       http://localhost:8080/oauth2/access_token?client_id=1001&client_secret=123456&redirect_uri=https://gitee.com/auth/csdn/callback&code=sdf8gsweb8s9fg8r&grant_type=authorization_code

       正确返回样例：
       {
            "access_token":"ACCESS_TOKEN",
            "token_type":"bearer",
            "expires_in":7200,
            "refresh_token":"REFRESH_TOKEN",
            "scope":"SCOPE",
            "openid":"OPENID"
        }

        错误返回样例：
        {
            "errcode":40029,
            "errmsg":"invalid code"
        }
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
            // 调用code换取accessToken接口
            // grant_type的不同会有不同的实现类,可以使用工厂模式,这里演示的是grant_type=authorization_code授权码模式根据code换取accessToken的情况.
            AccessTokenOuter accessTokenInfo = accessTokenService.getAccessTokenInfo(client_id, client_secret, redirect_uri, code, grant_type);
            if(null == accessTokenInfo){
                errorMap.put("errcode", CoreConstants.REQUEST_PROGRAM_ERROR_CODE);
                errorMap.put("errmsg", "系统异常");
                return errorMap;
            }

            resultMap.put("access_token", accessTokenInfo.getAccess_token());
            resultMap.put("token_type", accessTokenInfo.getToken_type());
            resultMap.put("expires_in", accessTokenInfo.getExpires_in());
            resultMap.put("refresh_token", accessTokenInfo.getRefresh_token());
            resultMap.put("scope", accessTokenInfo.getScope());
            resultMap.put("openid", accessTokenInfo.getOpenid());

            return resultMap;
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            errorMap.put("errcode", ex.getCode());
            errorMap.put("errmsg", ex.getMessage());
            return errorMap;
        }
    }

}