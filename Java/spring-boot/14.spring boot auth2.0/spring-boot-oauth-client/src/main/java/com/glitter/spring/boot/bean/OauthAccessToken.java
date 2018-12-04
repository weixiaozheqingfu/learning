package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class OauthAccessToken extends BaseBean {

    /** 主键 */
    private Long id;
    /** 用户对外开放id,对当前开发者帐号唯一 */
    private String openId;
    /** 用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。 */
    private String unionId;
    /** access_token */
    private String accessToken;
    /** access_token过期时间 */
    private Long expireIn;
    /** refresh_token */
    private String refreshToken;
    /** 授权作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
    /** 接口地址,授权多个接口地址用逗号（,）分隔 */
    private String interfaceUri;
    /** 第三方auth服务平台类型(如qq,wechart,sina等) */
    private String serverType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}