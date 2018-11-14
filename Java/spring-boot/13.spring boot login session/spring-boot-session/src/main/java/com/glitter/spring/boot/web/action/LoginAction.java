package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.observer.GlitterPublisher;
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
public class LoginAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IRsaService rsaService;

    @Autowired
    GlitterPublisher glitterPublisher;

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
     * 获取公钥
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseResult<String> login(@RequestBody LoginInfo paramBean) throws Exception {
        String pwd = rsaService.decrypt(paramBean.getPassword());
        logger.info(paramBean.getAccount() + ":" + pwd);
        return ResponseResult.success(pwd);
    }

    @RequestMapping(value = "/getLoginGraphCaptcha", method = RequestMethod.GET)
    public ResponseResult<String> getLoginGraphCaptcha() throws IOException {
        // 1.设置验证码属性
        int width = 110;
        int height = 35;
        CaptchaUtils captchaUtils = CaptchaUtils.getInstance();
        captchaUtils.set(width, height);

        // 2.生成验证码值并存入Session
        String graphCaptcha = captchaUtils.generateCaptchaCode();
        ISession session = sessionHandler.getSession();
        session.setAttribute("loginGraphCaptcha",graphCaptcha);

        // 3.将验证码图片输入到客户端
//        这种做法前端只要在img标签中指定src的链接路径即可。
//        ServletOutputStream sos = ResponseContext.get().getOutputStream();
//        ImageIO.write(captchaUtils.generateCaptchaImg(graphCaptcha), "jpg", sos);
//        sos.close();
//        return null;

        // 3.将验证码图片输入到客户端
//         这种做法是前端异步请求返回一个base64编码后的字符串,前端指定src="data:image/png;base64,后面加上本base64字符串即可"
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(captchaUtils.generateCaptchaImg(graphCaptcha), "jpg", out);
        return ResponseResult.success(new String(Base64.encodeBase64(out.toByteArray())));
    }

}