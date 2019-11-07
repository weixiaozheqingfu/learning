package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthAccessToken;

import java.util.List;

public interface IOauthAccessTokenDao{

    /**
    * 插入操作
    *
    * @param oauthAccessToken
    * @return
    */
    int insert(OauthAccessToken oauthAccessToken);

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
    * @param oauthAccessToken
    * @return
    */
    int delete(OauthAccessToken oauthAccessToken);

    /**
    * 根据主键更新
    *
    * @param oauthAccessToken
    * @return
    */
    int updateById(OauthAccessToken oauthAccessToken);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthAccessToken getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthAccessToken> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthAccessToken
     * @return
     */
    OauthAccessToken getOauthAccessToken(OauthAccessToken oauthAccessToken);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthAccessToken> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthAccessToken
     * @return
     */
    List<OauthAccessToken> findList(OauthAccessToken oauthAccessToken);

    /**
     * 按条件获取数量
     *
     * @param oauthAccessToken
     * @return
     */
    int getCount(OauthAccessToken oauthAccessToken);

}