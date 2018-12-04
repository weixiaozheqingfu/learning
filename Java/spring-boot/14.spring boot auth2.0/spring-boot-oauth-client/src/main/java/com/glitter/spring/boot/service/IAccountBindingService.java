package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccountBinding;

public interface IAccountBindingService {

    /**
     * 创建第三方账户绑定表; InnoDB free: 488448 kB
     * @param accountBinding
     */
    void create(AccountBinding accountBinding);

    /**
     * 修改第三方账户绑定表; InnoDB free: 488448 kB
     * @param accountBinding
     */
    void modifyById(AccountBinding accountBinding);

    /**
     * 根据主键删除第三方账户绑定表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取第三方账户绑定表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    AccountBinding getAccountBindingById(Long id);

}