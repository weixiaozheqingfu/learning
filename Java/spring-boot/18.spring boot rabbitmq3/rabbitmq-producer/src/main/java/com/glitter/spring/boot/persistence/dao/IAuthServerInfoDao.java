package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.AuthServerInfo;
import java.util.List;

public interface IAuthServerInfoDao{

    /**
    * 插入操作
    *
    * @param authServerInfo
    * @return
    */
    int insert(AuthServerInfo authServerInfo);

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
    * @param authServerInfo
    * @return
    */
    int delete(AuthServerInfo authServerInfo);

    /**
    * 根据主键更新
    *
    * @param authServerInfo
    * @return
    */
    int updateById(AuthServerInfo authServerInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    AuthServerInfo getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<AuthServerInfo> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param authServerInfo
     * @return
     */
    AuthServerInfo get(AuthServerInfo authServerInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<AuthServerInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param authServerInfo
     * @return
     */
    List<AuthServerInfo> findList(AuthServerInfo authServerInfo);

    /**
     * 按条件获取数量
     *
     * @param authServerInfo
     * @return
     */
    int getCount(AuthServerInfo authServerInfo);

}