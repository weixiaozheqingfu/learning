package com.glitter.spring.boot.context;

import org.slf4j.MDC;

public class ContextManager {

    public static void removeAllContext() {
        RequestContext.remove();
        ResponseContext.remove();
        MDC.clear();
    }

}
