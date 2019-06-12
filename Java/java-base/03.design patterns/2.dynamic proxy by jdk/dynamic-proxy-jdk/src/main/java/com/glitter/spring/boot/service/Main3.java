package com.glitter.spring.boot.service;

import com.glitter.spring.boot.service.impl.AliPayServiceImpl;
import com.glitter.spring.boot.service.impl.WeChatPayServiceImpl;
import com.glitter.spring.boot.service.proxy.TimeProxyInvocationHandler2;
import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Main3 extends AliPayServiceImpl {

    /** 这个main方法中的代码逻辑实际上应该是一个创建代理对象的工厂bean */
    public static void main(String[] args){
        // 传入目标对象

        // 获取目标对象的类声明信息和方法信息等关系到aop切面中可能用于判断该类以及该类的方法是否执行切面的规则。

        // 获取系统中被@Aspect标注的类的信息，形成一个map集合，每一个类形成一条map数据。
        // 每一个map数据中提取被@Before@After等注解标记的信息,最终形成一个过滤器规则map。

        // 遍历map，对map中的每一条记录，判断是否要生成目标对象针对于当前map所表示的这个切面类的代理对象。
        // 如果生成，那就生成.
        // 继续遍历下一个，此时的目标对象就转变为上一个刚生成的那个代理对象了。

        // 遍历完毕后，就可以拿到最终的代理对象了。


        WeChatPayServiceImpl weChatPayService = new WeChatPayServiceImpl();
        weChatPayService.setFlag(true);
        InvocationHandler h2 = new TimeProxyInvocationHandler2(weChatPayService);
        IPayService payService$proxy2 = (IPayService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IPayService.class}, h2);

        InvocationHandler h22 = new TimeProxyInvocationHandler2(payService$proxy2);

        IPayService payService$proxy22 = (IPayService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IPayService.class}, h22);
        payService$proxy22.pay(60L);

        createProxyClassFile("payService$proxy2", new Class[]{IPayService.class});





    }
















    /** 将生成的字节码保存到本地,方便理解代理模式的本质 */
    private static void createProxyClassFile(String proxyClassName, Class<?>[] interfaces) {
        byte[] data = ProxyGenerator.generateProxyClass(proxyClassName, interfaces);
        FileOutputStream out = null;
        try {
            String filePath = "E:\\workspaces-idea\\GITHUB\\learning\\Java\\java-base\\03.design patterns\\2.dynamic proxy by jdk\\dynamic-proxy-jdk\\src\\main\\java\\com\\glitter\\spring\\boot\\service\\impl\\" + proxyClassName + ".class";
            System.out.print(filePath);
            out = new FileOutputStream(filePath);
            out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}