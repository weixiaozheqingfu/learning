package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthServerEnum;
import java.util.List;

public interface IOauthServerEnumDao{

    /**
    * 插入操作
    *
    * @param oauthServerEnum
    * @return
    */
    int insert(OauthServerEnum oauthServerEnum);

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
    * @param oauthServerEnum
    * @return
    */
    int delete(OauthServerEnum oauthServerEnum);

    /**
    * 根据主键更新
    *
    * @param oauthServerEnum
    * @return
    */
    int updateById(OauthServerEnum oauthServerEnum);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthServerEnum getOauthServerEnumById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthServerEnum> getOauthServerEnumByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthServerEnum
     * @return
     */
    OauthServerEnum getOauthServerEnum(OauthServerEnum oauthServerEnum);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthServerEnum> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthServerEnum
     * @return
     */
    List<OauthServerEnum> findList(OauthServerEnum oauthServerEnum);

    /**
     * 按条件获取数量
     *
     * @param oauthServerEnum
     * @return
     */
    int getCount(OauthServerEnum oauthServerEnum);

}