package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.TemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateServiceFactory implements InitializingBean {

    @Autowired
    TemplateService demo1ServiceImpl;
    @Autowired
    TemplateService demo2ServiceImpl;

    private static final Map<String,TemplateService> map = new HashMap<>();
    @Override
    public void afterPropertiesSet() throws Exception {
        if(map.size()<=0){
            map.put("demo1", demo1ServiceImpl);
            map.put("demo2", demo2ServiceImpl);
        }
    }

    public TemplateService getTemplateServiceInstance(String demoType) {
        if (StringUtils.isBlank(demoType)) {
            return null;
        }
        TemplateService templateService = map.get(demoType);
        return templateService;
    }

}
