package com.glitter.spring.boot.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TypecAdapterHelper implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(TypecAdapterHelper.class);

    @Autowired
    TypecAdapterService hdmiAdapterServiceImpl;
    @Autowired
    TypecAdapterService microUsbAdapterServiceImpl;
    @Autowired
    TypecAdapterService mobileTypecAdapterServiceImpl;
    @Autowired
    TypecAdapterService vgaAdapterServiceImpl;

    private List<TypecAdapterService> typecAdapterServices;
    @Override
    public void afterPropertiesSet() throws Exception {
        typecAdapterServices = new ArrayList<>();
        typecAdapterServices.add(hdmiAdapterServiceImpl);
        typecAdapterServices.add(microUsbAdapterServiceImpl);
        typecAdapterServices.add(mobileTypecAdapterServiceImpl);
        typecAdapterServices.add(vgaAdapterServiceImpl);
    }

    public TypecAdapterService getTypecAdapterService(Object handler) {

        if (typecAdapterServices == null) {
            throw new RuntimeException("No adapter for handler [" + handler + "]: The TypecAdapterHelper needs to include typecAdapterServices that supports this handler");
        }

        for (TypecAdapterService typecAdapterService : typecAdapterServices) {
            if (logger.isInfoEnabled()) {
                logger.info("Testing typecAdapterService [" + typecAdapterService + "]");
            }
            if (typecAdapterService.supports(handler)) {
                return typecAdapterService;
            }
        }

        return null;
    }


}
