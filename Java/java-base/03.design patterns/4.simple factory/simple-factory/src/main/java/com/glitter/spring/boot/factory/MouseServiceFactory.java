package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.MouseService;
import com.glitter.spring.boot.service.impl.DellMouseServiceImpl;
import com.glitter.spring.boot.service.impl.HpMouseServiceImpl;
import com.glitter.spring.boot.service.impl.HuaweiMouseServiceImpl;
import org.apache.commons.lang3.StringUtils;

public class MouseServiceFactory {

    public MouseService getMouseServiceInstance(String mouseType) {
        if (StringUtils.isBlank(mouseType)) {
            return null;
        }
        if (mouseType.equals("hp")) {
            return new HpMouseServiceImpl();
        }
        if (mouseType.equals("dell")) {
            return new DellMouseServiceImpl();
        }
        if (mouseType.equals("huawei")) {
            return new HuaweiMouseServiceImpl();
        }
        return null;
    }

}
