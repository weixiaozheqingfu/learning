package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class OauthInterfaceEnum extends BaseBean {

    /** 主键 */
    private Long id;
    /** 接口名称 */
    private String interfaceName;
    /** 接口地址 */
    private String interfaceUri;
    /** 接口详细描述 */
    private String interfaceDesc;
    /** 授权范围名称 */
    private Long scopeName;
    /** 第三方auth服务平台类型(如qq,wechart,sina等) */
    private String serverType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}