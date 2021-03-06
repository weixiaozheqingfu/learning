package com.glitter.spring.boot.bean;

import lombok.Data;

@Data
public class AccessTokenOutParam extends BaseBean {

    /** 访问令牌 */
    private String access_token;

    /** jsessionid */
    private String jsessionid;

    /** 令牌类型 */
    private String token_type;

    /** 过期时间,单位为秒 */
    private Long expires_in;

    /** 更新令牌 */
    private String refresh_token;

    /** 权限范围 */
    private String scope;

    /** 授权用户唯一标识 */
    private Long userId;
 
}