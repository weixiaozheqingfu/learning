package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.DeveloperInfo;

public interface IDeveloperInfoService {

    /**
     * 创建开发者账号信息表; InnoDB free: 488448 kB
     * @param developerInfo
     */
    void create(DeveloperInfo developerInfo);

    /**
     * 修改开发者账号信息表; InnoDB free: 488448 kB
     * @param developerInfo
     */
    void modifyById(DeveloperInfo developerInfo);

    /**
     * 根据主键删除开发者账号信息表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取开发者账号信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    DeveloperInfo getDeveloperInfoById(Long id);

}