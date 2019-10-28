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
    private String scopeName;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}