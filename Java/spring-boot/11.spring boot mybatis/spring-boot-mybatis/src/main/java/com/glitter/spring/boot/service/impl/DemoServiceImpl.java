package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IDemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements IDemoService{

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public UserInfo getUserInfo(Long id) throws Exception {
        if(id<0){
            throw new BusinessException("-1", "参数异常");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setName("张三丰");
        userInfo.setAge(100);
        UserInfo userInfo1 = new UserInfo();
        return userInfo;
    }
}
