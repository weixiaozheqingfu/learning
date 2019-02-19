package com.glitter.spring.boot.service;


import com.glitter.spring.boot.bean.OauthClientConfig;

public interface IOauthClientConfigService {

    /**
     * 创建auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientConfig
     */
    void create(OauthClientConfig oauthClientConfig);

    /**
     * 修改auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientConfig
     */
    void modifyById(OauthClientConfig oauthClientConfig);

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
    OauthClientConfig getOauthClientConfigById(Long id);

}