package com.glitter.spring.boot.web.action;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientConfig;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.persistence.remote.IAuthRemote;
import com.glitter.spring.boot.service.IAuthService;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(AuthAction.class);

    @Autowired
    private IOauthClientConfigService oauthClientConfigService;

    @Autowired
    private IAuthService authService;

    @Autowired
    private IAuthRemote authRemote;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    /**
     * 重定向到wechat授权页
     * @return
     */
    @RequestMapping(value = "wechat", method = RequestMethod.GET)
    public String wechat() {
        return "redirect:https://open.weixin.qq.com/connect/qrconnect?appid=wx63d402790645b7e6&redirect_uri=https%3A%2F%2Fgitee.com%2Fauth%2Fwechat%2Fcallback&response_type=code&scope=snsapi_login&state=4348d1a94379cd9881a5b47dde6e47ccf5a856178674a4e6#wechat_redirect";
    }

    /**
     * 重定向到csdn授权页
     * @return
     */
    @RequestMapping(value = "csdn", method = RequestMethod.GET)
    public String csdn() {
        return "redirect:https://api.csdn.net/oauth2/authorize?client_id=1100511&redirect_uri=https%3A%2F%2Fgitee.com%2Fauth%2Fcsdn%2Fcallback&response_type=code&state=08c71949957fbbb0c1c0be816b46ed5a95ddcfb06b70bdad";
    }

    /**
     * 重定向到oauth_server授权页
     * @return
     */
    @RequestMapping(value = "oauth_server", method = RequestMethod.GET)
    public void oauth_server(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://www.baidu.com");
        OauthClientConfig oauthClientConfig = oauthClientConfigService.getOauthClientConfigByServerType("oauth_server");

        String client_id = oauthClientConfig.getClientId();
        String redirect_uri = oauthClientConfig.getRedirectUri();
        String scope = oauthClientConfig.getScope();
        String response_type = "code";
        String state = authService.generateState("oauth_server");

        StringBuffer url = new StringBuffer("http://localhost:8080/oauth2/authorize");
        url.append("?");
        url.append("client_id=" + client_id);
        url.append("redirect_uri=" + redirect_uri);
        url.append("scope=" + scope);
        url.append("response_type=" + response_type);
        url.append("state=" + state);

        response.sendRedirect(url.toString());
    }

    /**
     * wechat授权回调
     * @return
     */
    @RequestMapping(value = "wechat/callback", method = RequestMethod.GET)
    public String wechatCallback(@RequestParam String code, @RequestParam String state) {
        return null;
    }

    /**
     * csdn授权页授权回调
     * @return
     */
    @RequestMapping(value = "csdn/callback", method = RequestMethod.GET)
    public String csdnCallback(@RequestParam String code, @RequestParam String state) {
        return null;
    }

    /**
     * oauth_server授权回调
     * @return
     */
    @RequestMapping(value = "oauth_server/callback", method = RequestMethod.GET)
    public String oauth_server_callback(@RequestParam String code, @RequestParam String state) throws IOException {
        boolean stateStatus = authService.validateState(state, "oauth_server");

        // 1.属于恶意请求,重定向到登陆页面,并提示连接失败,请重试.
        if (!stateStatus) {
            return "redirect:http://localhost:8081/login.html?code=-1000";
        }

        // 2.获取accessToken,获取用户信息,看返回accessToken时返回的openid是否够用,如果够用,就不用再去请求获取用户信息接口了。
        OauthClientConfig oauthClientConfig = oauthClientConfigService.getOauthClientConfigByServerType("oauth_server");

        String client_id = oauthClientConfig.getClientId();
        String client_secret = oauthClientConfig.getClientSecret();
        String redirect_uri = oauthClientConfig.getRedirectUri();
        String grant_type= "authorization_code";

        Map map = null;
        try {
            map = authRemote.getOauthServerAccessToken(client_id, client_secret, redirect_uri, code, grant_type);
            logger.info("map:" + JSONObject.toJSONString(map));
        } catch (Exception e) {
            logger.error(JSONObject.toJSONString(e));
            return "redirect:http://localhost:8081/login.html?code=-1001";
        }
        if (null == map) {
            return "redirect:http://localhost:8081/login.html?code=-1002";
        }
        if (null != map.get("errcode")) {
            return "redirect:http://localhost:8081/login.html?code=-1003";
        }
        String access_token = (String)map.get("access_token");
        String token_type = (String)map.get("token_type");
        String expires_in = (String)map.get("expires_in");
        String refresh_token = (String)map.get("refresh_token");
        String scope = (String)map.get("scope");
        String openid = (String)map.get("openid");
        // 其他字段这里不一一判断了
        if (StringUtils.isBlank(access_token)) {
            return "redirect:http://localhost:8081/login.html?code=-1004";
        }

        // 3.accessToken入库
        OauthAccessToken oauthAccessToken = new OauthAccessToken();
        oauthAccessToken.setOpenId(openid);
        oauthAccessToken.setAccessToken(access_token);
        oauthAccessToken.setExpireIn(StringUtils.isBlank(expires_in) ? null : Long.valueOf(expires_in));
        oauthAccessToken.setRefreshToken(refresh_token);
        oauthAccessToken.setScope(scope);
        oauthAccessToken.setServerType("oauth_server");
        oauthAccessTokenService.create(oauthAccessToken);



        // TODO 验证三方登陆用户是否已经绑定了用户账户,如果没有绑定,则重定向到绑定页面(可以新注册账号并绑定,或者绑定已有账号),
        // TODO 如果已经绑定,则建立登陆会话,并重定向到首页


        return null;

        // 后续,登陆会话是独立的,与accessToken无关,如果只是借用第三方平台做第三方登陆,到此就为止了,
        // 如果说,应用中某些功能还要获取用户在第三方平台中的授权可访问的数据,那么就需要通过accessToken来获取数据即可,accessToken如果过期就使用refreshToken来换,但这也和当前的登陆会话没有任何关系.一码是一码.

        // 我们限制一个用户在同一种第三方平台下最多只能绑定一个账号,这就够用了,也是大多数三方登陆的做法,一个用户账号一条线,非常清晰。
        // 如果不做限制,一个账号在同一个三方平台绑定的多个账号会加重逻辑复杂度,加大出错概率,可能出现我们意想不到的漏洞,并且这也不是必要的和好的做法。
    }

}

