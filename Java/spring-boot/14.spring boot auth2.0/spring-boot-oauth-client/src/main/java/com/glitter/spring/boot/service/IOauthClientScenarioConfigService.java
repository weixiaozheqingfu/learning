package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthClientScenarioConfig;

public interface IOauthClientScenarioConfigService {

    /**
     * 创建auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param oauthClientScenarioConfig
     */
    void create(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
     * 修改auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param oauthClientScenarioConfig
     */
    void modifyById(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
     * 根据主键删除auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthClientScenarioConfig getOauthClientScenarioConfigById(Long id);

}