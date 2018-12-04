package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthClientScenarioConfig;
import java.util.List;

public interface IOauthClientScenarioConfigDao{

    /**
    * 插入操作
    *
    * @param oauthClientScenarioConfig
    * @return
    */
    int insert(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
    * 根据主键删除
    *
    * @param id
    * @return
    */
    int deleteById(Long id);

    /**
    * 根据主键集合删除
    *
    * @param ids
    * @return
    */
    int deleteByIds(Long[] ids);

    /**
    * 根据条件删除
    *
    * @param oauthClientScenarioConfig
    * @return
    */
    int delete(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
    * 根据主键更新
    *
    * @param oauthClientScenarioConfig
    * @return
    */
    int updateById(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthClientScenarioConfig getOauthClientScenarioConfigById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthClientScenarioConfig> getOauthClientScenarioConfigByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthClientScenarioConfig
     * @return
     */
    OauthClientScenarioConfig getOauthClientScenarioConfig(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthClientScenarioConfig> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthClientScenarioConfig
     * @return
     */
    List<OauthClientScenarioConfig> findList(OauthClientScenarioConfig oauthClientScenarioConfig);

    /**
     * 按条件获取数量
     *
     * @param oauthClientScenarioConfig
     * @return
     */
    int getCount(OauthClientScenarioConfig oauthClientScenarioConfig);

}