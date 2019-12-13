package com.glitter.spring.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

	private static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(logger.isInfoEnabled()) { logger.info("初始化ApplicationContext"); }
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(String beanId) throws BeansException {
		return (T) applicationContext.getBean(beanId);
	}

	public static <T> T getBean(Class<T> requiredType) {
		if(logger.isInfoEnabled()) { logger.info("获取对象：paramName:{} applicationContext:{}", requiredType.getName(), applicationContext); }
		return applicationContext.getBean(requiredType);
	}
	
	@Override
	public void destroy() throws Exception {
		applicationContext = null;
	}

}