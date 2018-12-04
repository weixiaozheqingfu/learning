package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class OauthScopeEnum extends BaseBean {

    /** 主键 */
    private Long id;
    /** 授权范围名称 */
    private Long scopeName;
    /** 授权范围描述 */
    private Long scopeDesc;
    /** 第三方auth服务平台类型(如qq,wechart,sina等) */
    private String serverType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}