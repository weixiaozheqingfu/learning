package com.glitter.spring.boot.context;

import com.glitter.spring.boot.service.impl.SessionContext;

public class ContextManager {

    public static void removeAllContext() {
        RequestContext.remove();
        ResponseContext.remove();
        JsessionIdCookieContext.remove();
        SessionContext.remove();
        AccessTokenInnerContext.remove();
    }

}
