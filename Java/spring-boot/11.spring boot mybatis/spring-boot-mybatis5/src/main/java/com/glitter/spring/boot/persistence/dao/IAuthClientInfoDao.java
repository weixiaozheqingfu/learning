package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.AuthClientInfo;
import java.util.List;

public interface IAuthClientInfoDao{

    /**
    * 插入操作
    *
    * @param authClientInfo
    * @return
    */
    int insert(AuthClientInfo authClientInfo);

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
    * @param authClientInfo
    * @return
    */
    int delete(AuthClientInfo authClientInfo);

    /**
    * 根据主键更新
    *
    * @param authClientInfo
    * @return
    */
    int updateById(AuthClientInfo authClientInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    AuthClientInfo getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<AuthClientInfo> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param authClientInfo
     * @return
     */
    AuthClientInfo get(AuthClientInfo authClientInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<AuthClientInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param authClientInfo
     * @return
     */
    List<AuthClientInfo> findList(AuthClientInfo authClientInfo);

    /**
     * 按条件获取数量
     *
     * @param authClientInfo
     * @return
     */
    int getCount(AuthClientInfo authClientInfo);

}