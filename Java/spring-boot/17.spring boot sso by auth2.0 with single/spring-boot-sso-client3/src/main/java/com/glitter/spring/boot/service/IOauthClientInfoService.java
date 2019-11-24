package com.glitter.spring.boot.service;


import com.glitter.spring.boot.bean.OauthClientInfo;

public interface IOauthClientInfoService {

    /**
     * 创建auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientInfo
     */
    void create(OauthClientInfo oauthClientInfo);

    /**
     * 修改auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientInfo
     */
    void modifyById(OauthClientInfo oauthClientInfo);

    /**
     * 根据主键删除auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthClientInfo getOauthClientInfoById(Long id);

    /**
     * 根据服务端类型获取对应的应用配置信息
     * @param serverType
     * @return
     */
    OauthClientInfo getOauthClientInfoByServerType(String serverType);

}