package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthScopeEnum;

import java.util.List;

public interface IOauthScopeEnumDao{

    /**
    * 插入操作
    *
    * @param oauthScopeEnum
    * @return
    */
    int insert(OauthScopeEnum oauthScopeEnum);

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
    * @param oauthScopeEnum
    * @return
    */
    int delete(OauthScopeEnum oauthScopeEnum);

    /**
    * 根据主键更新
    *
    * @param oauthScopeEnum
    * @return
    */
    int updateById(OauthScopeEnum oauthScopeEnum);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthScopeEnum getOauthScopeEnumById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthScopeEnum> getOauthScopeEnumByIds(Long[] ids);

    /**
     * 按主键集合查询
     *
     * @param scopeNames
     * @return
     */
    List<OauthScopeEnum> getOauthScopeEnumByScopeNames(List<String> scopeNames);

    /**
     * 按条件查询
     *
     * @param oauthScopeEnum
     * @return
     */
    OauthScopeEnum getOauthScopeEnum(OauthScopeEnum oauthScopeEnum);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthScopeEnum> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthScopeEnum
     * @return
     */
    List<OauthScopeEnum> findList(OauthScopeEnum oauthScopeEnum);

    /**
     * 按条件获取数量
     *
     * @param oauthScopeEnum
     * @return
     */
    int getCount(OauthScopeEnum oauthScopeEnum);

}