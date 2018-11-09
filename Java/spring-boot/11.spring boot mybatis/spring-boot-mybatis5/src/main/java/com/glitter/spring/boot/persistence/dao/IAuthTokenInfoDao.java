package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.AuthTokenInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAuthTokenInfoDao{

    /**
    * 插入操作
    *
    * @param authTokenInfo
    * @return
    */
    int insert(AuthTokenInfo authTokenInfo);

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
    * @param authTokenInfo
    * @return
    */
    int delete(AuthTokenInfo authTokenInfo);

    /**
    * 根据主键更新
    *
    * @param authTokenInfo
    * @return
    */
    int updateById(AuthTokenInfo authTokenInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    AuthTokenInfo getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<AuthTokenInfo> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param authTokenInfo
     * @return
     */
    AuthTokenInfo get(AuthTokenInfo authTokenInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<AuthTokenInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param authTokenInfo
     * @return
     */
    List<AuthTokenInfo> findList(AuthTokenInfo authTokenInfo);

    /**
     * 按条件获取数量
     *
     * @param authTokenInfo
     * @return
     */
    int getCount(AuthTokenInfo authTokenInfo);

}