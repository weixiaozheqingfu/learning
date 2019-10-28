package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthDeveloperRM;

import java.util.List;

public interface IOauthDeveloperRMDao{

    /**
    * 插入操作
    *
    * @param oauthDeveloperRM
    * @return
    */
    int insert(OauthDeveloperRM oauthDeveloperRM);

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
    * @param oauthDeveloperRM
    * @return
    */
    int delete(OauthDeveloperRM oauthDeveloperRM);

    /**
    * 根据主键更新
    *
    * @param oauthDeveloperRM
    * @return
    */
    int updateById(OauthDeveloperRM oauthDeveloperRM);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthDeveloperRM getOauthDeveloperRMById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthDeveloperRM> getOauthDeveloperRMByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthDeveloperRM
     * @return
     */
    OauthDeveloperRM getOauthDeveloperRM(OauthDeveloperRM oauthDeveloperRM);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthDeveloperRM> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthDeveloperRM
     * @return
     */
    List<OauthDeveloperRM> findList(OauthDeveloperRM oauthDeveloperRM);

    /**
     * 按条件获取数量
     *
     * @param oauthDeveloperRM
     * @return
     */
    int getCount(OauthDeveloperRM oauthDeveloperRM);

}