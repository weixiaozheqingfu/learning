package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthScopeEnum;

import java.util.List;

public interface IOauthScopeEnumService {

    /**
     * 创建授权作用域枚举表; InnoDB free: 488448 kB
     * @param oauthScopeEnum
     */
    void create(OauthScopeEnum oauthScopeEnum);

    /**
     * 修改授权作用域枚举表; InnoDB free: 488448 kB
     * @param oauthScopeEnum
     */
    void modifyById(OauthScopeEnum oauthScopeEnum);

    /**
     * 根据主键删除授权作用域枚举表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取授权作用域枚举表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthScopeEnum getById(Long id);

    List<OauthScopeEnum> getByScopeNames(List<String> scopeNames);

    List<OauthScopeEnum> getAll();

}