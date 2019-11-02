package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OauthAccessToken extends BaseBean {

    /** 主键 */
    private Long id;
    /** jsessionid */
    private String jsessionid;
    /** 用户id */
    private Long userId;
    /** access_token */
    private String accessToken;
    /** access_token过期时间 */
    private Long expireIn;
    /** refresh_token */
    private String refreshToken;
    /** 用户授权作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
    /** 用户授权scope对应的接口地址,授权多个接口地址用逗号（,）分隔 */
    private String interfaceUri;
    /** 第三方auth服务平台类型(如qq,wechart,sina,sso等) */
    private String serverType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}