package com.glitter.spring.boot.singleton;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 登记式单例模式是对一组单例模式进行的维护, 保证 map 中的对象是同一份 Spring 中使用的就是类似的模式:

 保证从注册中心取到的类的实例都是唯一且相同的实例。
 */
public class RegisterSingleton implements Serializable {

    /** 登记式单例模式, 保证map中的对象是同一份 */
    private static Map<String, Object> map;

    static {
        map = new ConcurrentHashMap<>();
    }

    private RegisterSingleton() {
        System.out.println("this Constructor is called");
    }

    public static <T> T getInstance(String name) {
        if (name == null) {
            return null;
        }
        if (map.get(name) == null) {
            try {
                map.put(name, Class.forName(name).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T)map.get(name);
    }

    public static <T> T getInstance(Class clazz) {
        if (clazz == null) {
            return null;
        }
        if (map.get(clazz.getName()) == null) {
            try {
                map.put(clazz.getName(), clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T)map.get(clazz.getName());
    }

}
