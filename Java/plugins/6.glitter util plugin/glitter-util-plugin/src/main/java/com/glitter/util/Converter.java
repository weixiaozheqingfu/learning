package com.glitter.util;

import com.glitter.bean.Page;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类转换器
 *
 * @author liudan@ucfgroup.com
 * @Title Converter
 * @Copyright: Copyright (c) 2016
 * @Company:
 * @Created on 2016/12/16
 */
public class Converter {

    public static <T> T transferObj(Object sourceClazz, Class<T> targetClazz) {
        T newB = null;
        if (targetClazz != null && sourceClazz != null) {
            newB = ModelUtils.copyProperties(sourceClazz, targetClazz);
        }
        return newB;
    }

    public static <B>List<B> transferToList(List<?> sourceClazz, Class<B> targetClazz) {
        List<B> newB = null;
        if (targetClazz != null && sourceClazz != null) {
            newB = ModelUtils.copyProperties(sourceClazz, targetClazz);
        }
        return newB;
    }
    public static <B> Page<B> transferPageListToPageList(Page<?> sourceClazz, Class<B> targetClazz) {
        Page<B> newB = null;
        if (targetClazz != null && sourceClazz != null) {
            newB = ModelUtils.copyProperties(sourceClazz, targetClazz);
        }
        return newB;
    }
    /**
     * 将对象装换为map
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }
    /**
     * 将对象装换为map
     * @param bean
     * @return
     */
    public static <T> Map<String, String> beanToMapNotNull(T bean) {
        Map<String, String> map = new HashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key)!=null) {
                    map.put(key + "", beanMap.get(key).toString());
                }
            }
        }
        return map;
    }
    /**
     * 将map装换为javabean对象
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

}
