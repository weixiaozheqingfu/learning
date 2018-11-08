package com.glitter.spring.boot.service;

//import com.github.pagehelper.PageInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.page.PageInfo;

public interface IUserInfoService {

    /**
     * 创建用户表; InnoDB free: 488448 kB
     * @param userInfo
     */
    void create(UserInfo userInfo);

    /**
     * 修改用户表; InnoDB free: 488448 kB
     * @param userInfo
     */
    void modifyById(UserInfo userInfo);

    /**
     * 根据主键删除用户表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取用户表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    UserInfo getUserInfoById(Long id);

    PageInfo<UserInfo> getUserInfosPage(Integer pageNum, Integer pageSize);

}