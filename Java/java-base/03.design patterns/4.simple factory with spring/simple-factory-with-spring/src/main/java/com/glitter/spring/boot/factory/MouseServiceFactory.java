package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.MouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MouseServiceFactory {

    @Autowired
    MouseService hpMouseServiceImpl;
    @Autowired
    MouseService dellMouseServiceImpl;
    @Autowired
    MouseService huaweiMouseServiceImpl;

    public MouseService getMouseServiceInstance(String mouseType) {

        if (StringUtils.isBlank(mouseType)) {
            return null;
        }
        if (mouseType.equals("hp")) {
            return hpMouseServiceImpl;
        }
        if (mouseType.equals("dell")) {
            return dellMouseServiceImpl;
        }
        if (mouseType.equals("huawei")) {
            return huaweiMouseServiceImpl;
        }
        return null;
    }

}
