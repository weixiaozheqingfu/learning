package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.service.ISession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

public class LoginAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    /**
     * 请求登陆页(与前端约定，该方法非ajax方法，用户浏览器直接访问，或前端代码重定向，重定向也是浏览器直接访问)
     * (非sso登录,则可以直接请求自己应用的login.html)
     * <p>
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
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
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseResult logout() throws Exception {
        // 1.获取session会话对象
        ISession session = sessionHandler.getSession();

        // 2.如果session会话对象中不存在用户信息,说明已经退出登录,返回成功即可(容错处理,这种情况不可能发生).
        UserInfo userInfo;
        if (null == (userInfo = (UserInfo) session.getAttribute(GlitterConstants.SESSION_USER))) {
            return ResponseResult.success(null);
        }

        // 3.注销session会话
        session.invalidate();
        return ResponseResult.success(null);
    }

}

