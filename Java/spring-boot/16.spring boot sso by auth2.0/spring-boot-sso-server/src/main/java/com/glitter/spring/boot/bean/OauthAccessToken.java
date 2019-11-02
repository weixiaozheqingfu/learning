package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OauthAccessToken extends BaseBean {

    /** 主键ID */
    private Long id;
    /** jsessionid */
    private String jsessionid;
    /** 用户id */
    private Long userId;
    /** 应用id */
    private String clientId;
    /** 授权作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
    /** 接口地址,授权多个接口地址用逗号（,）分隔 */
    private String interfaceUri;
    /** access_token类型,bearer类型或mac类型 */
    private String tokenType;
    /** access_token */
    private String accessToken;
    /** access_token过期时长,单位秒 */
    private Long accessTokenExpireIn;
    /** access_token过期时间 */
    private Date accessTokenExpireTime;
    /** 刷新token */
    private String refreshToken;
    /** refresh_token过期时长,单位秒 */
    private Long refreshTokenExpireIn;
    /** access_token过期时间 */
    private Date refreshTokenExpireTime;
    /** 0:未删除 1：已删除 */
    private Boolean deleteFlag;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;
 
}