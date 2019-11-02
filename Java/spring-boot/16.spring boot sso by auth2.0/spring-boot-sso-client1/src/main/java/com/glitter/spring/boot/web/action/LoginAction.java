package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IRsaService;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.util.CaptchaUtils;
import com.glitter.spring.boot.web.param.LoginInfo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseResult<String> login(@RequestBody LoginInfo paramBean) throws Exception {
        UserInfo userInfo = null;
        sessionHandler.getSession().setAttribute(GlitterConstants.SESSION_USER, userInfo);
        return ResponseResult.success(null);
    }

    /**
     * 退出
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseResult logout() throws Exception {
        // 1.获取session会话对象
        ISession session = sessionHandler.getSession();

        // 2.如果session会话对象中不存在用户信息,说明已经退出登录,返回成功即可(容错处理,这种情况不可能发生).
        UserInfo userInfo;
        if (null == (userInfo = (UserInfo)session.getAttribute(GlitterConstants.SESSION_USER))){
            return ResponseResult.success(null);
        }

        // 3.注销session会话
        session.invalidate();
        return ResponseResult.success(null);
    }

}

