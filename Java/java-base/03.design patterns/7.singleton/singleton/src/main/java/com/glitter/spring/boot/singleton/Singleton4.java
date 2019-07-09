package com.glitter.spring.boot.singleton;

/**
 4、双检锁/双重校验锁（DCL，即 double-checked locking）

 JDK 版本：JDK1.5 起
 是否 Lazy 初始化：是
 是否多线程安全：是
 实现难度：较复杂

 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 getInstance() 的性能对应用程序很关键。



 这种写法就解决了Singleton1类中的线程安全问题,因为线程安全问题只出现在第一次调用时的高并发情况.
 而第一次调用时候,只有singleton == null的时候才可能产生并发问题,好,那么就在singleton == null的时候,在方法体内部使用同步代码块,
 保证第一次创建对象时候的次序,这样就解决了首次singleton == null时的线程安全问题.
 而后续再有线程进来时,发现singleton != null，会直接返回单例对象。

 同时这种做法也避免了第二种的因噎废食的情况，不加区分的全部加上同步的情况,当前的这种写法,只会在第一次调用时候,走同步代码块,会慢一点,但就慢这一次.
 后续再去获取单例对象时就不走同步代码块,就很快了。


 推荐使用(思路非常好)

 */
public class Singleton4 {

    private volatile static Singleton4 instance;

    private Singleton4 (){

    }

    public static Singleton4 getInstance() {
        if (instance == null) {
            synchronized (Singleton4.class) {
                if (instance == null) {
                    instance = new Singleton4();
                }
            }
        }
        return instance;
    }

    public void sysout() {
        System.out.println("当前实例对象:" + instance);
    }

}
