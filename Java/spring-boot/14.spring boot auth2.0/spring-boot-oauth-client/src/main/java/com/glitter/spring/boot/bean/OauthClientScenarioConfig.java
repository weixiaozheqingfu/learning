package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class OauthClientScenarioConfig extends BaseBean {

    /** 主键 */
    private Long id;
    /** 第三方auth服务平台分配的客户端id */
    private String clientId;
    /** 声明第三方auth服务平台响应模式,使第三方auth平台知道客户端想使用哪一种响应模式,auth平台存在两种响应模式即code,token */
    private String responseType;
    /** 声明第三方auth服务平台授权类型,使第三方auth平台知道客户端想使用哪一种授权类型进行授权流程,auth服务器端都会有对应的逻辑处理,auth平台存在的授权模式共5种,如authorization_code,implicit,refresh_token等 */
    private String grantType;
    /** 针对于第三方auth服务的众多授权作用域,客户端应用声明需要用户确认授权的作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
    /** state参数后缀值,不同的值表示不同的含义,具体含义由客户端自行定义使用逻辑 */
    private String stateType;
    /** state_type描述 */
    private String stateTypeDesc;
    /** 自定义不同场景下获取accessToken成功后的响应地址 */
    private String responseUrl;
    /** 业务场景名称 */
    private String scenarioName;
    /** 业务场景说明 */
    private String scenarioDesc;
    /** 第三方auth服务平台类型(如qq,wechart,sina等) */
    private String serverType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}