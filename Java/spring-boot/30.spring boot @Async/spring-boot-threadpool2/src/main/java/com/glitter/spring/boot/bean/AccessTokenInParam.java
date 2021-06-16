package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.List;

@Data
public class AccessTokenInParam extends BaseBean {

    /** 访问令牌 */
    private String access_token;

    /** jsessionid */
    private String jsessionid;

    /** 客户id */
    private String clientId;

    /** 授权用户id */
    private Long userId;

    /** 接口权限范围 */
    private List<String> interfaceUri;

    /** access_tokens剩余有效期,单位秒 */
    private Long remainingExpirationTime;

}