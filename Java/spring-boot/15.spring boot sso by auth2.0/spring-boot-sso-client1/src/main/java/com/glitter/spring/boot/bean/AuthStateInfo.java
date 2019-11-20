package com.glitter.spring.boot.bean;

import lombok.Data;

@Data
public class AuthStateInfo extends BaseBean {

    /** 第三方auth服务平台分配的客户端id */
    private String clientId;
    /** 客户端应用回调地址 */
    private String redirectUri;
    /** 针对于第三方auth服务的众多授权作用域,客户端应用声明需要用户确认授权的作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
}