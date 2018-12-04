package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IRsaService;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.service.IUserInfoService;
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

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IRsaService rsaService;

    /**
     * 获取公钥
     * @return
     */
    @RequestMapping(value = "getPublicKey", method = RequestMethod.GET)
    public ResponseResult<String> getPublicKey() {
        String publicKey = rsaService.getPublicKey();
        return ResponseResult.success(publicKey);
    }

    /**
     * 获取图形验证码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getLoginGraphCaptcha", method = RequestMethod.GET)
    public ResponseResult<String> getLoginGraphCaptcha() throws IOException {
        // 1.设置验证码属性
        int width = 110;
        int height = 35;
        CaptchaUtils captchaUtils = CaptchaUtils.getInstance();
        captchaUtils.set(width, height);

        // 2.生成验证码值并存入Session
        String graphCaptcha = captchaUtils.generateCaptchaCode();
        sessionHandler.getSession().setAttribute("loginGraphCaptcha",graphCaptcha);

        // 3.将验证码图片输入到客户端
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(captchaUtils.generateCaptchaImg(graphCaptcha), "jpg", out);
        return ResponseResult.success(new String(Base64.encodeBase64(out.toByteArray())));
    }

    /**
     * 登陆
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseResult<String> login(@RequestBody LoginInfo paramBean) throws Exception {
        // 1.参数基本验证
        if (null == paramBean) { throw new BusinessException("-1","请输入账号和密码"); }
        if (null == paramBean.getAccount()) { throw new BusinessException("-1","请输入账号"); }
        if (null == paramBean.getPassword()) { throw new BusinessException("-1","请输入密码"); }
        if (null == paramBean.getGraphCaptcha()) { throw new BusinessException("-1","请输入图形验证码"); }

        // 2.验证图形验证码
        String loginGraphCaptcha = (String)sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_LOGIN_GRAPHCAPTCHA);
        sessionHandler.getSession().removeAttribute(GlitterConstants.SESSION_LOGIN_GRAPHCAPTCHA);
        if(!paramBean.getGraphCaptcha().equals(loginGraphCaptcha)){
            throw new BusinessException("-2","图形验证码输入错误");
        }

        // 3.密码解密
        String pwd = null;
        try {
            pwd = rsaService.decrypt(paramBean.getPassword());
        } catch (Exception e) {
            logger.error("密码解密失败");
            throw new BusinessException("-1","密码错误");
        }

        // 4.验证用户名密码
        UserInfo userInfo = null;
        //UserInfo userInfo = userInfoService.getByAccount(paramBean.getAccount());
        //if(null == userInfo){ throw new BusinessException("-1","用户名或密码错误"); }
        //if (!pwd.equals(userInfo.getPassword())) { throw new BusinessException("-1", "用户名或密码错误"); }

        // 5.记录用户登录会话信息
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

