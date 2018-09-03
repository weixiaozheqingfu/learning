package com.glitter.spring.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 如果使用外置tomcat启动,则需要添加此类,外置tomcat通过spi机制加载项目,详见spring-web-5.0.4.RELEASE.jar包中的META-INF/services/
 * javax.servlet.ServletContainerInitializer接口的实现类是org.springframework.web.SpringServletContainerInitializer
 * 容器启动时会调用该实现类SpringServletContainerInitializer中的onStartup方法,该方法会初始化项目的web环境,如过滤器,拦截器,servlet等,
 * 同时onStartup方法内部调用了WebApplicationInitializer接口的方法做相关事宜,SpringBootServletInitializer是WebApplicationInitializer接口的抽象实现类,
 * 我们自定义的SpringBootStartApplication类继承自这个抽象类,所以在外置tomcat容器启动时,我们的类的configure会被加载调用到。
 * 调用到之后,包括完整的onStartup方法和configure方法,整个过程其实都是spring boot框架利用外置taocat或者说是servlet 3.0的api在做web项目的初始化工作,这样就可以省去web.xml。
 * 也就是说将容器直接通过web.xml加载项目的方式,变化为spring boot利用spi机制和sevlet3.0的新特性,变化为tomcat容器委托给实现了javax.servlet.ServletContainerInitializer接口的实现类来完成
 * 容器中项目的加载,只是这个实现类是spring boot而已.既然实现类是springboot,那么后续的事情包括大量的约定或者注解就是springboot内部的事情了。
 *
 * 如果只是使用spring boot的内置tomcat启动,则只需要Application类即可,内置tomcat是spring boot自主实现的,并没有使用spi机制来加载项目。
 */
public class SpringBootStartApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(Application.class);
    }

}
