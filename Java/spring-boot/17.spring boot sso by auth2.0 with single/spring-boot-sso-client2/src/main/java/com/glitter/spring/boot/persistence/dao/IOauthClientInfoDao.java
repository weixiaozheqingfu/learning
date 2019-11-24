package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthClientInfo;

import java.util.List;

public interface IOauthClientInfoDao {

    /**
    * 插入操作
    *
    * @param oauthClientInfo
    * @return
    */
    int insert(OauthClientInfo oauthClientInfo);

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
    * @param oauthClientInfo
    * @return
    */
    int delete(OauthClientInfo oauthClientInfo);

    /**
    * 根据主键更新
    *
    * @param oauthClientInfo
    * @return
    */
    int updateById(OauthClientInfo oauthClientInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthClientInfo getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthClientInfo> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthClientInfo
     * @return
     */
    OauthClientInfo getOauthClientInfo(OauthClientInfo oauthClientInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthClientInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthClientInfo
     * @return
     */
    List<OauthClientInfo> findList(OauthClientInfo oauthClientInfo);

    /**
     * 按条件获取数量
     *
     * @param oauthClientInfo
     * @return
     */
    int getCount(OauthClientInfo oauthClientInfo);

}