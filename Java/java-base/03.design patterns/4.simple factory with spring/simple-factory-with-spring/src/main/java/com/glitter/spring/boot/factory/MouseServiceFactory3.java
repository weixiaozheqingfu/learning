package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.MouseService;
import com.glitter.spring.boot.service.impl.DellMouseServiceImpl;
import com.glitter.spring.boot.service.impl.HpMouseServiceImpl;
import com.glitter.spring.boot.service.impl.HuaweiMouseServiceImpl;
import com.glitter.spring.boot.util.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MouseServiceFactory3 implements InitializingBean {

    @Autowired
    ApplicationContextHolder applicationContextHolder;

    private static final Map<String,MouseService> map = new HashMap<>();
    @Override
    public void afterPropertiesSet() throws Exception {
        if(map.size()<=0){
            map.put("hp", applicationContextHolder.getBean(HpMouseServiceImpl.class));
            map.put("dell", applicationContextHolder.getBean(DellMouseServiceImpl.class));
            map.put("huawei", applicationContextHolder.getBean(HuaweiMouseServiceImpl.class));
        }
    }

    public MouseService getMouseServiceInstance(String mouseType) {
        if (StringUtils.isBlank(mouseType)) {
            return null;
        }
        MouseService mouseService = map.get(mouseType);
        return mouseService;
    }

}
