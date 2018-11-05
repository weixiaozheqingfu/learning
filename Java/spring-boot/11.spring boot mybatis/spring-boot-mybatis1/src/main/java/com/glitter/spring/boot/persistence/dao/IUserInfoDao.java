package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.UserInfo;
import java.util.List;

public interface IUserInfoDao{

    /**
    * 插入操作
    *
    * @param userInfo
    * @return
    */
    int insert(UserInfo userInfo);

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
    * @param userInfo
    * @return
    */
    int delete(UserInfo userInfo);

    /**
    * 根据主键更新
    *
    * @param userInfo
    * @return
    */
    int updateById(UserInfo userInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    UserInfo getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<UserInfo> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param userInfo
     * @return
     */
    UserInfo get(UserInfo userInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<UserInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param userInfo
     * @return
     */
    List<UserInfo> findList(UserInfo userInfo);

    /**
     * 按条件获取数量
     *
     * @param userInfo
     * @return
     */
    int getCount(UserInfo userInfo);

}