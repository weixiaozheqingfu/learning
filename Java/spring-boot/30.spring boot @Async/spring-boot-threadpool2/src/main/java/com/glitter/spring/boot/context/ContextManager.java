package com.glitter.spring.boot.context;

public class ContextManager {

    /**
     * 这里放适合一起remove的,如果不适合的,那就再自己单独remove
     */
    public static void removeAllContext() {
        RequestContext.remove();
        ResponseContext.remove();
    }

}
