package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.List;

@Data
public class AccessTokenInner extends BaseBean {

    /** 访问令牌 */
    private String access_token;

    /** 会话id */
    private String jsessionId;

    /** 客户id */
    private String clientId;

    /** 授权用户id */
    private Long userId;

    /** 接口权限范围 */
    private List<String> interfaceUri;

}