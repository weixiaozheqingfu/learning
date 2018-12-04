package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthClientRM;

import java.util.List;

public interface IOauthClientRMDao{

    /**
    * 插入操作
    *
    * @param oauthClientRM
    * @return
    */
    int insert(OauthClientRM oauthClientRM);

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
    * @param oauthClientRM
    * @return
    */
    int delete(OauthClientRM oauthClientRM);

    /**
    * 根据主键更新
    *
    * @param oauthClientRM
    * @return
    */
    int updateById(OauthClientRM oauthClientRM);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthClientRM getOauthClientRMById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthClientRM> getOauthClientRMByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthClientRM
     * @return
     */
    OauthClientRM getOauthClientRM(OauthClientRM oauthClientRM);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthClientRM> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthClientRM
     * @return
     */
    List<OauthClientRM> findList(OauthClientRM oauthClientRM);

    /**
     * 按条件获取数量
     *
     * @param oauthClientRM
     * @return
     */
    int getCount(OauthClientRM oauthClientRM);

}