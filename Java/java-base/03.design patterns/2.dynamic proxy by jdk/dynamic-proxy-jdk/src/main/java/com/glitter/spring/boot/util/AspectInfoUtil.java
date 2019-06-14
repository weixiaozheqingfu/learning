package com.glitter.spring.boot.util;

import com.glitter.spring.boot.service.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class AspectInfoUtil {

    public static void main(String[] args) throws ClassNotFoundException {
        AspectInfoUtil.getAspectInfos();
    }

    /**
     * 获取aspect切面配置信息列表
     */
    public static List<AspectInfo> getAspectInfos() throws ClassNotFoundException {
        List<AspectInfo> aspectInfos = new ArrayList<>();

        // 扫描com.glitter.spring.boot包下所有类
        List<String> classNamesAll = PackageUtil.getClassName("com.glitter.spring.boot");

        // 从classNames中过滤出使用@Aspect注解标记的类
        List<String> classNames = AspectInfoUtil.getClassNames(classNamesAll);

        // 遍历classNames,提取AspectInfo信息
        for (int i = 0; i < classNames.size(); i++) {
            AspectInfo aspectInfo = new AspectInfo();
            aspectInfo.setAspectName(classNames.get(i));

            Class c = Class.forName(classNames.get(i));
            Method[] ms = c.getMethods();
            for (Method m : ms) {
                Annotation[] annotations = m.getAnnotations();
                if (null == annotations || annotations.length <= 0) {
                    continue;
                }
                for (int j = 0; j < annotations.length; j++) {
                    if (annotations[j].annotationType() == Before.class) {
                        aspectInfo.setBefore(((Before)annotations[j]).pointcut());
                        break;
                    } else if (annotations[j].annotationType() == Around.class) {
                        aspectInfo.setAround(((Around)annotations[j]).pointcut());
                        break;
                    } else if (annotations[j].annotationType() == After.class) {
                        aspectInfo.setAfter(((After)annotations[j]).pointcut());
                        break;
                    } else if (annotations[j].annotationType() == AfterReturning.class) {
                        aspectInfo.setAfterReturning(((AfterReturning) annotations[j]).pointcut());
                        break;
                    } else if (annotations[j].annotationType() == AfterThrowing.class) {
                        aspectInfo.setAfterThrowing(((AfterThrowing)annotations[j]).pointcut());
                        break;
                    }
                }
            }
            aspectInfos.add(aspectInfo);
        }
        return aspectInfos;
    }


    private static List<String> getClassNames(List<String> classNamesAll) throws ClassNotFoundException {
        List<String> classNames = new ArrayList<>();
        for (int i = 0; i < classNamesAll.size(); i++) {
            Class c = Class.forName(classNamesAll.get(i));
            Annotation[] annotations = c.getAnnotations();
            if (null == annotations || annotations.length <= 0) {
                continue;
            }
            for (int j = 0; j < annotations.length; j++) {
                if (annotations[j].annotationType() == Aspect.class) {
                    classNames.add(classNamesAll.get(i));
                    break;
                }
            }
        }
        return classNames;
    }
}