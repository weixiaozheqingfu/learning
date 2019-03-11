package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.AuthStateInfo;
import com.glitter.spring.boot.bean.OauthClientConfig;
import com.glitter.spring.boot.service.IAuthService;
import com.glitter.spring.boot.service.IOauthClientConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(AuthAction.class);

    @Autowired
    private IOauthClientConfigService oauthClientConfigService;

    @Autowired
    private IAuthService authService;

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
        AuthStateInfo authStateInfo = new AuthStateInfo();
        authStateInfo.setClientId(oauthClientConfig.getClientId());
        authStateInfo.setRedirectUri(oauthClientConfig.getRedirectUri());
        authStateInfo.setScope(oauthClientConfig.getScope());

        String client_id = oauthClientConfig.getClientId();
        String redirect_uri = oauthClientConfig.getRedirectUri();
        String scope = oauthClientConfig.getScope();
        String response_type = "code";
        String state = authService.generateState(authStateInfo);

        StringBuffer url = new StringBuffer("http://localhost:8080/oauth2/authorize");
        url.append("?");
        url.append("client_id=" + client_id);
        url.append("redirect_uri=" + redirect_uri);
        url.append("scope=" + scope);
        url.append("response_type=" + response_type);
        url.append("state=" + state);

        response.sendRedirect(url.toString());
    }

}

