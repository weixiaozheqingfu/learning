package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthCode;

import java.util.List;

public interface IOauthCodeDao{

    /**
    * 插入操作
    *
    * @param oauthCode
    * @return
    */
    int insert(OauthCode oauthCode);

    /**
    * 根据主键删除
    *
    * @param id
    * @return
    */
    int deleteById(Long id);

    int deleteByCode(String code);

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
    * @param oauthCode
    * @return
    */
    int delete(OauthCode oauthCode);

    /**
    * 根据主键更新
    *
    * @param oauthCode
    * @return
    */
    int updateById(OauthCode oauthCode);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthCode getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthCode> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthCode
     * @return
     */
    OauthCode get(OauthCode oauthCode);

    /**
     * 按条件查询
     * @param userId
     * @param clientId
     * @return
     */
    OauthCode getByUserIdAndClient(Long userId, String clientId);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthCode> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthCode
     * @return
     */
    List<OauthCode> findList(OauthCode oauthCode);

    /**
     * 按条件获取数量
     *
     * @param oauthCode
     * @return
     */
    int getCount(OauthCode oauthCode);

}