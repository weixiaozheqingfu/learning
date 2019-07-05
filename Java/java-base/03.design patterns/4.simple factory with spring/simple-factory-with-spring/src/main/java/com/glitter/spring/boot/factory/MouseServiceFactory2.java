package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.MouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 推荐这种写法
 */
@Component
public class MouseServiceFactory2 implements InitializingBean {

    @Autowired
    MouseService hpMouseServiceImpl;
    @Autowired
    MouseService dellMouseServiceImpl;
    @Autowired
    MouseService huaweiMouseServiceImpl;

    private static final Map<String,MouseService> map = new HashMap<>();
    @Override
    public void afterPropertiesSet() throws Exception {
        if(map.size()<=0){
            map.put("hp", hpMouseServiceImpl);
            map.put("dell", dellMouseServiceImpl);
            map.put("huawei", huaweiMouseServiceImpl);
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
