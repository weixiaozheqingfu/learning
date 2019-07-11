package com.glitter.spring.boot.adapter;

import com.glitter.spring.boot.adapter.impl.HdmiAdapterServiceImpl;
import com.glitter.spring.boot.adapter.impl.MicroUsbAdapterServiceImpl;
import com.glitter.spring.boot.adapter.impl.MobileTypecAdapterServiceImpl;
import com.glitter.spring.boot.adapter.impl.VgaAdapterServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TypecAdapterHelper {
    private static final Logger logger = LoggerFactory.getLogger(TypecAdapterHelper.class);

    private static final HdmiAdapterServiceImpl hdmiAdapterServiceImpl = new HdmiAdapterServiceImpl();
    private static final MicroUsbAdapterServiceImpl microUsbAdapterServiceImpl = new MicroUsbAdapterServiceImpl();
    private static final MobileTypecAdapterServiceImpl mobileTypecAdapterServiceImpl = new MobileTypecAdapterServiceImpl();
    private static final VgaAdapterServiceImpl vgaAdapterServiceImpl = new VgaAdapterServiceImpl();

    private static final List<TypecAdapterService> typecAdapterServices = new ArrayList();

    static {
        typecAdapterServices.add(hdmiAdapterServiceImpl);
        typecAdapterServices.add(microUsbAdapterServiceImpl);
        typecAdapterServices.add(mobileTypecAdapterServiceImpl);
        typecAdapterServices.add(vgaAdapterServiceImpl);
    }

    public static TypecAdapterService getTypecAdapterService(Object handler) {

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
