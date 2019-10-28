package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.DeveloperInfo;

import java.util.List;

public interface IDeveloperInfoDao{

    /**
    * 插入操作
    *
    * @param developerInfo
    * @return
    */
    int insert(DeveloperInfo developerInfo);

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
    * @param developerInfo
    * @return
    */
    int delete(DeveloperInfo developerInfo);

    /**
    * 根据主键更新
    *
    * @param developerInfo
    * @return
    */
    int updateById(DeveloperInfo developerInfo);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    DeveloperInfo getDeveloperInfoById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<DeveloperInfo> getDeveloperInfoByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param developerInfo
     * @return
     */
    DeveloperInfo getDeveloperInfo(DeveloperInfo developerInfo);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<DeveloperInfo> findAllList();

    /**
     * 按条件查询
     *
     * @param developerInfo
     * @return
     */
    List<DeveloperInfo> findList(DeveloperInfo developerInfo);

    /**
     * 按条件获取数量
     *
     * @param developerInfo
     * @return
     */
    int getCount(DeveloperInfo developerInfo);

}