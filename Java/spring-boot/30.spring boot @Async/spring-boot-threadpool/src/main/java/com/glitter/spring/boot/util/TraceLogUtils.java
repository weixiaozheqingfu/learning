package com.glitter.spring.boot.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Lazy(false)
public class TraceLogUtils {

	private static Logger logger = LoggerFactory.getLogger(TraceLogUtils.class);

	private static ApplicationContext applicationContext;

	public static String getTraceId(){
		// RandomStringUtils
		return UUID.randomUUID().toString();
	}

}