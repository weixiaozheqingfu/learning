package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OauthClientConfig extends BaseBean {

    /** 主键 */
    private Long id;
    /** 第三方auth服务平台分配的客户端id */
    private String clientId;
    /** 第三方auth服务平台分配的客户端密码 */
    private String clientSecret;
    /** 客户端应用回调地址 */
    private String redirectUri;
    /** 针对于第三方auth服务的众多授权作用域,客户端应用声明需要用户确认授权的作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
    /** 第三方auth服务平台类型(如qq,wechart,sina,csdn,github等) */
    private String serverType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}