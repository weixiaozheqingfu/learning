package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.OauthInterfaceEnum;
import java.util.List;

public interface IOauthInterfaceEnumDao{

    /**
    * 插入操作
    *
    * @param oauthInterfaceEnum
    * @return
    */
    int insert(OauthInterfaceEnum oauthInterfaceEnum);

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
    * @param oauthInterfaceEnum
    * @return
    */
    int delete(OauthInterfaceEnum oauthInterfaceEnum);

    /**
    * 根据主键更新
    *
    * @param oauthInterfaceEnum
    * @return
    */
    int updateById(OauthInterfaceEnum oauthInterfaceEnum);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    OauthInterfaceEnum getOauthInterfaceEnumById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<OauthInterfaceEnum> getOauthInterfaceEnumByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param oauthInterfaceEnum
     * @return
     */
    OauthInterfaceEnum getOauthInterfaceEnum(OauthInterfaceEnum oauthInterfaceEnum);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<OauthInterfaceEnum> findAllList();

    /**
     * 按条件查询
     *
     * @param oauthInterfaceEnum
     * @return
     */
    List<OauthInterfaceEnum> findList(OauthInterfaceEnum oauthInterfaceEnum);

    /**
     * 按条件获取数量
     *
     * @param oauthInterfaceEnum
     * @return
     */
    int getCount(OauthInterfaceEnum oauthInterfaceEnum);

}