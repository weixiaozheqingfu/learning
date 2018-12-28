package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.List;

@Data
public class AccessTokenInner extends BaseBean {

    /** 客户id */
    private String clientId;

    /** 授权用户id */
    private Long userId;

    /** 授权用户唯一标识 */
    private String openId;

    /** 授权用户统一标识 */
    private String unionId;

    /** 接口权限范围 */
    private List<String> interfaceUri;

}