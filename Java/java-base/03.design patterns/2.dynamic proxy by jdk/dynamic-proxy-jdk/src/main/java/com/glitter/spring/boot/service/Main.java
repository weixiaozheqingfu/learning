package com.glitter.spring.boot.service;

import com.glitter.spring.boot.service.impl.*;
import com.glitter.spring.boot.service.proxy.TimeProxyInvocationHandler;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * 即便是使用接口方式实现静态代理，静态代理也有其不可克服的缺点。
 缺点就是局限性，现在我只有一个IpayService接口,这个接口需要时间和日志的代理，我们可以写两个代理实现类实现IpayService接口。
 而我的系统中可能会有很过多个其他的Service接口，他们的实现类需要有时间和日志功能的增强，那怎么办，每一个接口我都要
 去写时间和日志的代理类去实现这个接口，这会造成项目中代理类数量的极速膨胀，并且这些代理类的内容都是一模一样的。

 既然静态代理有这些缺点无法克服，为什么我们还要学习静态代理呢。
 须知只是是有渐进性的，先学习静态代理是为我们后续学习动态代理打下一个基础，在学习动态代理时各方面都容易很多。
 再有就是静态代理相对动态代理简单，也不是完全没有使用场景，比如我的代理只是想在某一个service上生效，其他service不生效，那我可以使用静态代理的，没有问题。
 */
public class Main extends AliPayServiceImpl {

    public static void main(String[] args){
        TimeProxyInvocationHandler h = new TimeProxyInvocationHandler();
        IPayService payService$proxy = (IPayService) Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[]{IPayService.class}, h);
        // 其实代理对象就是把接口的所有方法都重新实现了一把,动态生成一个代理类的class类字节码文件并通过classLoader加载到jvm虚拟机内存中,进而生成代理对象实例
        // 这个代理对象实际上就是在每个接口方法中都只调用h.invoke()方法而已,在h.invoke()方法中可以做任何事情。h.invoke()方法其实就是代理方法了，可以做任何事情
        // 可以在调用实际对象前后做任何事情，当然，如果你愿意，你都可以不调用实际对象的方法。不过那样就有点耍流氓了啊。
        payService$proxy.pay(50L);

        createProxyClassFile("payService$proxy$Proxy", new Class[]{IPayService.class});
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
