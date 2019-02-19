package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthClientConfig;

import java.util.List;

public interface IOauthClientConfigDao{

    /**
    * 插入操作
    *
    * @param oauthClientConfig
    * @return
    */
    int insert(OauthClientConfig oauthClientConfig);

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
    * @param oauthClientConfig
    * @return
    */
    int delete(OauthClientConfig oauthClientConfig);

    /**
    * 根据主键更新
    *
    * @param oauthClientConfig
    * @return
    */
    int updateById(OauthClientConfig oauthClientConfig);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthClientConfig getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthClientConfig> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthClientConfig
     * @return
     */
    OauthClientConfig getOauthClientConfig(OauthClientConfig oauthClientConfig);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthClientConfig> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthClientConfig
     * @return
     */
    List<OauthClientConfig> findList(OauthClientConfig oauthClientConfig);

    /**
     * 按条件获取数量
     *
     * @param oauthClientConfig
     * @return
     */
    int getCount(OauthClientConfig oauthClientConfig);

}