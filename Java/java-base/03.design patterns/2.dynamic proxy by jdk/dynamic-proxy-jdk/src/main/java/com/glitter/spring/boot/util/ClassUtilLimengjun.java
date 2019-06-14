package com.glitter.spring.boot.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;


public class ClassUtilLimengjun {

    /**
     * 获取类是否被public修饰符所修饰
     */
    public static boolean isPublicClass(Object obj){
        Class clazz = obj.getClass();
        return Modifier.isPublic(clazz.getModifiers());
    }

    /**
     * 获取类中所有公有方法的方法名集合
     */
    public static List<String> getPublicMethodNames(Object obj) {
        List<String> result = new ArrayList<>();
        Class c = obj.getClass();
        Method [] ms = c.getMethods();
        for (Method m : ms) {
            result.add(m.getName());
        }
        return result;
    }

    /**
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Method getMethod(Object obj, String methodName){
        Class c = obj.getClass();
        Method [] ms = c.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }
}