package cn.huimin100.erp.util;

import cn.huimin100.erp.bean.Page;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.util.StopWatch;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 复制对象
 */
@Log4j2
public class ModelUtils extends BeanUtils {

    /**
     * 复制分页对象
     *
     * @param sourcePage  原始pageList
     * @param targetClass 复制的目标类型的pageList class类型
     * @return
     */
    public static <T, B> Page<B> copyProperties(Page<T> sourcePage, Class<B> targetClass) {
        try {
            if (sourcePage == null) {
                return null;
            }
            Page newPage = sourcePage.getClass().newInstance();
            newPage.setPageNum(sourcePage.getPageNum());
            newPage.setPageSize(sourcePage.getPageSize());
            newPage.setTotalCount(sourcePage.getTotalCount());
            ArrayList<B> newContent = new ArrayList<>();
            newPage.setTotalData(sourcePage.getTotalData());
            if(sourcePage.getData() != null){
                StopWatch stopWatch = null;
                stopWatch = new StopWatch();
                stopWatch.start();
                for (Object source : sourcePage.getData()) {
                    Object obj = copyProperties(source, targetClass);
                    newContent.add((B) obj);
                }
                stopWatch.stop();
                log.info("[复制对象listSize:{0},Time(S):{1}]", newContent.size(), stopWatch.getTotalTimeSeconds());
            }
            newPage.setData(newContent);
            return newPage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制list对象
     *
     * @param sourceList
     * @param targetClass
     * @return
     */
    @SuppressWarnings("unused")
    public static <T> List<T> copyProperties(List<?> sourceList, Class<T> targetClass) {
        try {

            if (sourceList == null) {
                return new ArrayList<T>(1);
            }
            List<T> newList = new ArrayList<T>(sourceList.size());
            for (Object source : sourceList) {
                T newInstance = targetClass.newInstance();
                copyProperties(source, newInstance);
                newList.add(newInstance);
            }
            return newList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 深层复制对象
     *
     * @param sourceObject 原始对象
     * @param targetClass  目标对象类型
     * @return
     */
    public static <T> T copyProperties(Object sourceObject, Class<T> targetClass) {
        try {
            T newInstance = targetClass.newInstance();
            if (sourceObject == null) {
                return newInstance;
            }
            copyProperties(sourceObject, newInstance);
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void copyProperties(Object source, Object target) throws BeansException {
        copyProperties(source, target, null, null);
    }

    public static void copyProperties(Object source, Object target, String[] ignoreProperties)
            throws BeansException {

        copyProperties(source, target, null, ignoreProperties);
    }

    public static void copyProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties)
            throws BeansException {

        if (source != null && target != null) {
            Class<?> actualEditable = target.getClass();
            if (editable != null) {
                if (!editable.isInstance(target)) {
                    throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                            "] not assignable to Editable class [" + editable.getName() + "]");
                }
                actualEditable = editable;
            }
            PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
            List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

            for (PropertyDescriptor targetPd : targetPds) {
                if (targetPd.getWriteMethod() != null &&
                        (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                    PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                    if (sourcePd != null && sourcePd.getReadMethod() != null) {
                        try {
                            Method readMethod = sourcePd.getReadMethod();
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            // 修改自定义转换Long 转Date
                            if (value != null &&
                                    (readMethod.getReturnType().equals(Long.class) || readMethod.getReturnType().equals(Integer.class))
                                    && targetPd.getReadMethod().getReturnType().equals(Date.class)) {
                                Long dateTmp = (Long) value;
                                Long timestamp = Long.valueOf(dateTmp) * 1000;
                                value = new Date(timestamp);
                            }
                            //修改自定义转换Integer 转Double
                            if (value != null &&
                                    (readMethod.getReturnType().equals(Integer.class))
                                    && targetPd.getReadMethod().getReturnType().equals(Double.class)) {
                                Double dateTmp = Double.valueOf(value.toString());
                                value = dateTmp;
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            log.error("对象复制:" + source.getClass().getName() + "-->>" + target.getClass().getName()
                                    + ",属性:" + targetPd.getName(), ex);
                            //throw new FatalBeanException("Could not copy properties from source to target", ex);
//                            continue;
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
    }
}
