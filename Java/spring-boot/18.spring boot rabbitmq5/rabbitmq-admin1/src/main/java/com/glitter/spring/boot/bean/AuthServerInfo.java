package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class AuthServerInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 第三方auth服务平台id */
    private String serverId;
    /** 第三方auth服务平台名称(如qq,微信,新浪微博等) */
    private String serverName;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}