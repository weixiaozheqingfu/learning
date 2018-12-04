package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class AccountBinding extends BaseBean {

    /** 主键 */
    private Long id;
    /** 用户表id */
    private Long userId;
    /** 用户对外开放id,对当前开发者帐号唯一 */
    private String openId;
    /** 用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。不一定所有第三方都有此字段. */
    private String unionId;
    /** 第三方auth服务平台类型(如qq,wechart,sina等) */
    private String serverType;
    /** 绑定时间 */
    private Date bindTime;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}