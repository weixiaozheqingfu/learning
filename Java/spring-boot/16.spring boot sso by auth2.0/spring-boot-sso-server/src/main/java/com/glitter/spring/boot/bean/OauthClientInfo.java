package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OauthClientInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 客户端应用id */
    private String clientId;
    /** 客户端应用秘钥 */
    private String clientSecret;
    /** 客户端应用名称 */
    private String clientName;
    /** 客户端应用回调地址 */
    private String redirectUri;
    /** 客户端应用注销登录地址 */
    private String logoutUri;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}