package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthClientBaseConfig;
import java.util.List;

public interface IOauthClientBaseConfigDao{

    /**
    * 插入操作
    *
    * @param oauthClientBaseConfig
    * @return
    */
    int insert(OauthClientBaseConfig oauthClientBaseConfig);

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
    * @param oauthClientBaseConfig
    * @return
    */
    int delete(OauthClientBaseConfig oauthClientBaseConfig);

    /**
    * 根据主键更新
    *
    * @param oauthClientBaseConfig
    * @return
    */
    int updateById(OauthClientBaseConfig oauthClientBaseConfig);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthClientBaseConfig getOauthClientBaseConfigById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthClientBaseConfig> getOauthClientBaseConfigByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthClientBaseConfig
     * @return
     */
    OauthClientBaseConfig getOauthClientBaseConfig(OauthClientBaseConfig oauthClientBaseConfig);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthClientBaseConfig> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthClientBaseConfig
     * @return
     */
    List<OauthClientBaseConfig> findList(OauthClientBaseConfig oauthClientBaseConfig);

    /**
     * 按条件获取数量
     *
     * @param oauthClientBaseConfig
     * @return
     */
    int getCount(OauthClientBaseConfig oauthClientBaseConfig);

}