package com.glitter.spring.boot.singleton;

/**
 7、双检锁/双重校验锁（DCL，即 double-checked locking）

 JDK 版本：JDK1.5 起
 是否 Lazy 初始化：是
 是否多线程安全：是
 实现难度：较复杂

 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 getInstance() 的性能对应用程序很关键。



 我们推荐使用Singleton4,但是即便是Singleton4也有不完美的地方.
 1.如果是使用反射是可以创建Singleton4的实例对象的,这个时候的实例对象每创建一个就是一个,这样产生的对象就不是系统中独一个了。是的，是会存在这个问题。
   所以说，如果你执意要这么用，那是你的问题了，自己跟自己较劲也没办法啊。但是代码中倒是也可以做一定的控制，比如Singleton7中的私有构造方法中的这种处理。
   但是本人不建议这么做，单例搞这么复杂没有必要，自己在代码中不使用反射来new这个单例对象不就好了，单例是用来用的，不是用来玩的。

 2.如果单例类实现了序列化接口，那么反序列化后的对象会是一个新的对象。都单例模式了，干嘛还要序列化,但是这个问题是可以解决的。

 不推荐使用(思路非常好)

 */
public class Singleton7 {

    private volatile static Singleton7 instance;

    private Singleton7(){
        if (null != instance) {
            throw new RuntimeException("Singleton7 instance has exit!");
        }
        instance = this;
    }

    public static Singleton7 getInstance() {
        if (instance == null) {
            synchronized (Singleton7.class) {
                if (instance == null) {
                    instance = new Singleton7();
                }
            }
        }
        return instance;
    }

    public void sysout() {
        System.out.println("当前实例对象:" + instance);
    }

}
