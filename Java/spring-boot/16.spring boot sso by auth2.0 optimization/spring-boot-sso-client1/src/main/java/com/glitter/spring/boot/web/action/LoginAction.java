package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.context.JsessionIdCookieContext;
import com.glitter.spring.boot.persistence.remote.ISsoRemote;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    ISsoRemote ssoRemote;
    @Autowired
    IOauthAccessTokenService oauthAccessTokenService;

    /**
     * 请求登陆页(与前端约定，该方法非ajax方法，用户浏览器直接访问，或前端代码重定向，重定向也是浏览器直接访问)
     * (非sso登录,则可以直接请求自己应用的login.html)
     * <p>
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.sendRedirect("/oauth/sso/authorize");
        return;
    }

//    登录方法去除，不走自己的登录，走sso登录流程。
//    /**
//     * 登陆
//     * @return
//     */
//    @RequestMapping(value = "login", method = RequestMethod.POST)
//    public ResponseResult<String> login(@RequestBody LoginInfo paramBean) throws Exception {
//        // 验证paramBean中的用户名密码等信息
//        UserInfo userInfo = null;
//        // ...
//        sessionHandler.getSession().setAttribute(GlitterConstants.SESSION_USER, userInfo);
//        return ResponseResult.success(null);
//    }

    /**
     * 退出
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseResult logout() throws Exception {
        // 1.获取当前局部会话对应的accessToken
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenService.getOauthAccessTokenByJsessionid(JsessionIdCookieContext.get());

        // 2.删除当前局部会话绑定的access_token记录
        oauthAccessTokenService.deleteByJsessionid(oauthAccessTokenDb.getJsessionid());

        // 3.注销局部会话
        sessionHandler.getSession(oauthAccessTokenDb.getJsessionid()).invalidate();

        // 4.通知sso用户中心注销全局会话
        try {
            ssoRemote.logout(oauthAccessTokenDb.getAccessToken());
        } catch (Exception e) {
            // 如果出现失败的情况,则应该发邮件预警,并调查bug了。
            e.printStackTrace();
            return ResponseResult.fail("-100","操作失败，请联系管理员！", true);
        }

        // 5.跳转到登录页
        String loginUrl = GlitterConstants.DOMAIN_SSO_CLIETN1 + "/login";
        return ResponseResult.success(loginUrl);
    }

}

