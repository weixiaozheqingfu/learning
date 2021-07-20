package cn.huimin100.erp.service;

import cn.huimin100.erp.dto.LoginUserDTO;

public interface ILoginUserService {

    /**
     * 获取登录用户信息
     * @return
     * @throws Exception
     */
    LoginUserDTO getLoginUserInfo();

}
