package com.glitter.spring.boot.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassDemo05 {
    public static void main(String[] args) {
        Class<?> c1 = null;

        try {
            c1 = Class.forName("com.glitter.spring.boot.util.Person");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("-----------------获取实现的全部接口----------------");
        // 获取实现的全部接口
        Class<?> interfaces[] = c1.getInterfaces();
        for (Class<?> thisclass : interfaces) {
            System.out.println(thisclass.getName());
        }

        System.out.println("-----------------取得父类----------------");
        Class<?> superclass = c1.getSuperclass();
        System.out.println(superclass.getName());

        System.out.println("-----------------取得一个类中的全部的构造方法----------------");
        Constructor<?> constructors[] = c1.getConstructors();
        for (int i = 0; i < constructors.length; i++) {
            Class<?> parameterTypes[] = constructors[i].getParameterTypes();
            //取得权限
            int modifiers = constructors[i].getModifiers();
            //还原权限(public static String toString(int mod))
            System.out.println(Modifier.toString(modifiers));
            //输出构造方法名称
            String name = constructors[i].getName();
            //循环打印参数类型
            for (int j = 0; j < parameterTypes.length; j++) {
                System.out.println(parameterTypes[j]);
                System.out.println(modifiers);
                System.out.println(name);
            }

        }
        System.out.println("-----------------------取得全部的方法-------------------------");
        //获取全部的方法
        Method[] methods = c1.getMethods();
        for (int j = 0; j < methods.length; j++) {
            System.out.println(methods[j]);
            //得到方法的返回值类型
            Class<?> returnType = methods[j].getReturnType();
            System.out.println(returnType);
            //得到全部参数的类型
            Class<?>[] parameterTypes2 = methods[j].getParameterTypes();
            for (Class<?> class1 : parameterTypes2) {
                System.out.println(class1);
            }
            //获得全部异常抛出
            Class<?>[] exceptionTypes = methods[j].getExceptionTypes();
            //判断是否有异常
            if(exceptionTypes.length>0)
            {
                System.out.println(") throws ");
            }else{
                System.out.println(")");
            }
            for (int i = 0; i < exceptionTypes.length; i++) {
                System.out.println(exceptionTypes[i].getName());
                if(i<exceptionTypes.length-1)
                {
                    System.out.println(",");
                }
            }
            System.out.println();
        }
        System.out.println("==================取得本类全部属性==================");
        //得到本类中的属性
        Field[] declaredFields = c1.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            //取得本类中属性类型
            Class<?> type = declaredFields[i].getType();
            System.out.println(type);
            //得到修饰符的数字
            int modifiers = declaredFields[i].getModifiers();
            System.out.println(modifiers);
            //取得属性的修饰符
            String priv = Modifier.toString(modifiers);
            System.out.println(priv);

        }
        System.out.println("==================取得父类全部属性==================");
        Field[] fields = c1.getFields();
        for (int i = 0; i < fields.length; i++) {
            Class<?> type = fields[i].getType();
            System.out.println(type);
            int modifiers = fields[i].getModifiers();
            System.out.println(modifiers);
            String priv = Modifier.toString(modifiers);
            System.out.println(priv);
        }
    }
}