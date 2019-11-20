package com.glitter.spring.boot.web.param;

import com.glitter.spring.boot.bean.BaseBean;
import lombok.Data;

@Data
public class LoginInfo extends BaseBean {

    /** 账号 */
    private String account;
    /** 密码 */
    private String password;
    /** 图形验证码 */
    private String graphCaptcha;
 
}