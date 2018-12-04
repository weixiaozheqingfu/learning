package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthClientBaseConfig;

public interface IOauthClientBaseConfigService {

    /**
     * 创建auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientBaseConfig
     */
    void create(OauthClientBaseConfig oauthClientBaseConfig);

    /**
     * 修改auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientBaseConfig
     */
    void modifyById(OauthClientBaseConfig oauthClientBaseConfig);

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
    OauthClientBaseConfig getOauthClientBaseConfigById(Long id);

}