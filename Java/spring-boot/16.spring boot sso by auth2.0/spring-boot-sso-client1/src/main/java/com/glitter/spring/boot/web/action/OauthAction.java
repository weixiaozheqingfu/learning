package com.glitter.spring.boot.web.action;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.persistence.remote.ISsoRemote;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.IOauthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/oauth")
public class OauthAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthAction.class);

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Autowired
    private IOauthService oauthService;

    @Autowired
    private ISsoRemote ssoRemote;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    /**
     * 重定向到sso授权页
     * @return
     */
    @RequestMapping(value = "sso/authorize", method = RequestMethod.GET)
    public void sso_server(HttpServletResponse response) throws IOException {
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByServerType("sso");

        String client_id = oauthClientInfo.getClientId();
        String redirect_uri = oauthClientInfo.getRedirectUri();
        String scope = oauthClientInfo.getScope();
        String response_type = "code";
        String state = oauthService.generateState("sso");

        StringBuffer url = new StringBuffer("http://localhost:8080/sso/authorize");
        url.append("?");
        url.append("&client_id=" + client_id);
        url.append("&redirect_uri=" + redirect_uri);
        url.append("&scope=" + scope);
        url.append("&response_type=" + response_type);
        url.append("&state=" + state);

        response.sendRedirect(url.toString());
    }

    /**
     * sso授权回调
     * @return
     */
    @RequestMapping(value = "sso/callback", method = RequestMethod.GET)
    public String oauth_server_callback(@RequestParam String code, @RequestParam String state) throws IOException {
        boolean stateStatus = oauthService.validateState(state, "sso");
        // 1.属于恶意请求,重定向到登陆页面,并提示连接失败,请重试.
        if (!stateStatus) {
            return "redirect:http://localhost:8081/error.html?code=-1000";
        }

        // 2.code换accessToken
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByServerType("sso");
        String client_id = oauthClientInfo.getClientId();
        String client_secret = oauthClientInfo.getClientSecret();
        String redirect_uri = oauthClientInfo.getRedirectUri();
        String grant_type= "authorization_code";

        Map map = null;
        try {
            map = ssoRemote.getAccessToken(client_id, client_secret, redirect_uri, code, grant_type);
            logger.info("map:" + JSONObject.toJSONString(map));
        } catch (Exception e) {
            logger.error(JSONObject.toJSONString(e));
            return "redirect:http://localhost:8081/error.html?code=-1001";
        }
        if (null == map) {
            return "redirect:http://localhost:8081/error.html?code=-1002";
        }
        if (null != map.get("errcode")) {
            return "redirect:http://localhost:8081/error.html?code=-1003";
        }
        String access_token = map.get("access_token") == null ? null : String.valueOf(map.get("access_token"));
        String token_type = map.get("access_token") == null ? null : String.valueOf(map.get("access_token"));
        String expires_in = map.get("expires_in") == null ? null : String.valueOf(map.get("expires_in"));
        String refresh_token = map.get("refresh_token") == null ? null : String.valueOf(map.get("refresh_token"));
        String scope = map.get("scope") == null ? null : String.valueOf(map.get("scope"));
        Long userId = map.get("userid") == null ? null : Long.valueOf(String.valueOf(map.get("userid")));
        // 其他字段这里不一一判断了
        if (StringUtils.isBlank(access_token)) {
            return "redirect:http://localhost:8081/error.html?code=-1004";
        }

        // 3.组装accessToken对象
        OauthAccessToken oauthAccessToken = new OauthAccessToken();
        oauthAccessToken.setUserId(userId);
        oauthAccessToken.setAccessToken(access_token);
        oauthAccessToken.setExpireIn(StringUtils.isBlank(expires_in) ? null : Long.valueOf(expires_in));
        oauthAccessToken.setRefreshToken(refresh_token);
        oauthAccessToken.setScope(scope);
        oauthAccessToken.setServerType("sso");

        // 4.获取用户信息
        UserInfo userInfo = ssoRemote.getUerInfo(access_token);
        if (userInfo == null) {
            return "redirect:http://localhost:8081/error.html?code=-1005";
        }

        // 5.创建会话
        sessionHandler.getSession().setAttribute(GlitterConstants.SESSION_USER, userInfo);
        oauthAccessToken.setJsessionid(sessionHandler.getSession().getId());

        // 6.accessToken对象绑定jsessionid并入库(保证code换accessToken的过程具备反复执行的能力)
        try {
            OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByJsessionid(sessionHandler.getSession().getId());
            if (null == oauthAccessTokenDb) {
                oauthAccessTokenService.create(oauthAccessToken);
            } else {
                oauthAccessTokenDb.setUserId(oauthAccessToken.getUserId());
                oauthAccessTokenDb.setAccessToken(oauthAccessToken.getAccessToken());
                oauthAccessTokenDb.setExpireIn(oauthAccessToken.getExpireIn());
                oauthAccessTokenDb.setRefreshToken(oauthAccessToken.getRefreshToken());
                oauthAccessTokenDb.setScope(oauthAccessToken.getScope());
                oauthAccessTokenDb.setServerType(oauthAccessToken.getServerType());
                oauthAccessTokenDb.setJsessionid(oauthAccessToken.getJsessionid());
                oauthAccessTokenDb.setUpdateTime(new Date());
                oauthAccessTokenService.modifyById(oauthAccessTokenDb);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sessionHandler.getSession().invalidate();
            return "redirect:http://localhost:8081/error.html?code=-1006";
        }

        // 7.完成登录后重定向到首页
        return "redirect:http://localhost:8081/index.html";
    }

    /**
     * sso回调注销客户端会话。
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sso/logout", method = RequestMethod.GET)
    public ResponseResult logout(@RequestParam String access_token) throws IOException {
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByAccessToken(access_token);
        oauthAccessTokenService.deleteByJsessionid(oauthAccessTokenDb.getJsessionid());
        sessionHandler.getSession(oauthAccessTokenDb.getJsessionid()).invalidate();
        return ResponseResult.success(true);
    }
}

