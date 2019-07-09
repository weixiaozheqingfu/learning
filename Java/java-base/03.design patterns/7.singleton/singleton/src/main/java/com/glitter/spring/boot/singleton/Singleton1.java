package com.glitter.spring.boot.singleton;

/**
 1、懒汉式，线程不安全

 是否 Lazy 初始化：是
 是否多线程安全：否
 实现难度：易
 描述：这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。因为没有加锁 synchronized，所以严格意义上它并不算单例模式。
 这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作。


 其实所谓的线程不安全，意思是说，可能两个线程同时在调用getInstance()方法,由于间隔极度小,而instance又是静态的,系统独一份,是共享资源,两个线程太近的话，
 第一个线程判断instance == null成立,会进入方法体,但还没有执行方法体,此时线程切换到第二个线程,第二个线程此时判断instance == null也是成立的，也会进入方法体,
 这样,第一个线程和第二个线程会依次执行方法体:instance = new Singleton1();
 这样,Singleton就会在系统中创建两个实例对象,最终instance会引用最后一次创建的那个对象,
 这其实对于程序运行来说没有什么不安全的,但是,既然是单例模式,这样,就不满足单例了,所以说线程不安全.
 事实上,这种情况出现的概率及其之低,只出现在首次调用时的高并发可能会出现一次这样的情况,因为一旦对象建立,后续就不存在什么线程不安全的问题了,
 后续多线程调用时,都会判断发现线程已经存在,直接返回实例对象.

 但是话说回来,无论如何也是有漏洞,并且后面我们也有更好的方式,那我们就不用这种方式了呗。


 不推荐

 */
public class Singleton1 {

    private static Singleton1 instance;

    private Singleton1(){

    }

    public static Singleton1 getInstance() {
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }

    public void sysout() {
        System.out.println("当前实例对象:" + instance);
    }
}
