package com.glitter.spring.boot.persistence.dao;

import com.glitter.spring.boot.bean.AccountBinding;

import java.util.List;

public interface IAccountBindingDao{

    /**
    * 插入操作
    *
    * @param accountBinding
    * @return
    */
    int insert(AccountBinding accountBinding);

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
    * @param accountBinding
    * @return
    */
    int delete(AccountBinding accountBinding);

    /**
    * 根据主键更新
    *
    * @param accountBinding
    * @return
    */
    int updateById(AccountBinding accountBinding);

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    AccountBinding getById(Long id);

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    List<AccountBinding> getByIds(Long[] ids);

    /**
     * 按条件查询
     *
     * @param accountBinding
     * @return
     */
    AccountBinding getAccountBinding(AccountBinding accountBinding);

    /**
     * 查询所有记录
     *
     * @return
     */
    List<AccountBinding> findAllList();

    /**
     * 按条件查询
     *
     * @param accountBinding
     * @return
     */
    List<AccountBinding> findList(AccountBinding accountBinding);

    /**
     * 按条件获取数量
     *
     * @param accountBinding
     * @return
     */
    int getCount(AccountBinding accountBinding);

}