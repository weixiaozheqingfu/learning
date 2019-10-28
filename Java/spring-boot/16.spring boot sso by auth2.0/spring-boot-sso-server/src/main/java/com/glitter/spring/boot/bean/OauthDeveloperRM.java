package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class OauthDeveloperRM extends BaseBean {

    /** 主键 */
    private Long id;
    /** 开发者用户id */
    private Long developerId;
    /** 用户id */
    private Long userId;
    /** 用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。 */
    private String unionId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}