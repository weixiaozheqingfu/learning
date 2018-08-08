package com.glitter.springboot.base;

import com.glitter.springboot.base.filter.HelloWorldFilter;
import com.glitter.springboot.base.servlet.HelloWorldServlet;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Set;

public class CustomServletContainerInitializer implements ServletContainerInitializer {

    private final static String JAR_HELLO_URL = "/hello";

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) {
        System.out.println("创建 helloWorldServlet...");

        ServletRegistration.Dynamic servlet = servletContext.addServlet(HelloWorldServlet.class.getSimpleName(), HelloWorldServlet.class);
        servlet.addMapping(JAR_HELLO_URL);

        System.out.println("创建 helloWorldFilter...");

        FilterRegistration.Dynamic filter = servletContext.addFilter(HelloWorldFilter.class.getSimpleName(), HelloWorldFilter.class);

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        dispatcherTypes.add(DispatcherType.REQUEST);
        dispatcherTypes.add(DispatcherType.FORWARD);

        filter.addMappingForUrlPatterns(dispatcherTypes, true, JAR_HELLO_URL);
    }
}