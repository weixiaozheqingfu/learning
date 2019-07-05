package com.glitter.spring.boot.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PcServiceFactoryProducer {

    @Autowired
    PcServiceAbstractFactory hpPcServiceFactory;
    @Autowired
    PcServiceAbstractFactory dellPcServiceFactory;
    @Autowired
    PcServiceAbstractFactory huaweiPcServiceFactory;

    public PcServiceAbstractFactory getPcServiceFactory(String pcType) {
        if (StringUtils.isBlank(pcType)) {
            return null;
        }
        if (pcType.equals("hp")) {
            return hpPcServiceFactory;
        }
        if (pcType.equals("dell")) {
            return dellPcServiceFactory;
        }
        if (pcType.equals("huawei")) {
            return huaweiPcServiceFactory;
        }
        return null;
    }

}
