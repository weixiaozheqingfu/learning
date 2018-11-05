package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class AuthServerInterfaceInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 第三方auth服务端信息表主键 */
    private Long authServerInfoId;
    /** 接口名称 */
    private String interfaceName;
    /** 接口地址 */
    private String interfaceUrl;
    /** 接口说明 */
    private String interfaceRemark;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}