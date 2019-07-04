package com.glitter.spring.boot.factory;

import org.apache.commons.lang3.StringUtils;

public class PcServiceFactoryProducer {

    public PcServiceAbstractFactory getPcServiceFactory(String pcType) {
        if (StringUtils.isBlank(pcType)) {
            return null;
        }
        if (pcType.equals("hp")) {
            return new HpPcServiceFactory();
        }
        if (pcType.equals("dell")) {
            return new DellPcServiceFactory();
        }
        if (pcType.equals("huawei")) {
            return new HuaweiPcServiceFactory();
        }
        return null;
    }

}
