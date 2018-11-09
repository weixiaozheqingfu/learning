package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.AuthServerInterfaceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAuthServerInterfaceInfoDao{

    /**
    * 插入操作
    *
    * @param authServerInterfaceInfo
    * @return
    */
    int insert(AuthServerInterfaceInfo authServerInterfaceInfo);

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
    * @param authServerInterfaceInfo
    * @return
    */
    int delete(AuthServerInterfaceInfo authServerInterfaceInfo);

    /**
    * 根据主键更新
    *
    * @param authServerInterfaceInfo
    * @return
    */
    int updateById(AuthServerInterfaceInfo authServerInterfaceInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    AuthServerInterfaceInfo getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<AuthServerInterfaceInfo> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param authServerInterfaceInfo
     * @return
     */
    AuthServerInterfaceInfo get(AuthServerInterfaceInfo authServerInterfaceInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<AuthServerInterfaceInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param authServerInterfaceInfo
     * @return
     */
    List<AuthServerInterfaceInfo> findList(AuthServerInterfaceInfo authServerInterfaceInfo);

    /**
     * 按条件获取数量
     *
     * @param authServerInterfaceInfo
     * @return
     */
    int getCount(AuthServerInterfaceInfo authServerInterfaceInfo);

}