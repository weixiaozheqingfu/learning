package com.glitter.spring.boot.singleton;

/**
 3、饿汉式

 是否 Lazy 初始化：否
 是否多线程安全：是
 实现难度：易
 描述：这种方式比较常用，但容易产生垃圾对象。
 优点：没有加锁，执行效率会提高。
 缺点：类加载时就初始化，浪费内存。
 它基于 classloader 机制避免了多线程的同步问题，不过，instance 在类装载时就实例化，虽然导致类装载的原因有很多种，在单例模式中大多数都是调用 getInstance 方法，
 但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，这时候初始化 instance 显然没有达到 lazy loading 的效果。


 这种方式其实可用用了,因为如果单例模式的这个类是我们自己写的,我们势必是要在系统中使用他的,即使被意外提前加载了,没有做到懒加载又如何呢。
 它是单例的，系统中就这独一份，并且我们确定系统中要用他，否则我们也不会写这个类了,所以我觉得关系不大。
 事实上讲，我们系统中单例模式的类不会太多，系统中提前加载了一个类资源，会造成多大的影响呢？


 可以用(非最优,但可以用)

 */
public class Singleton3 {

    private static Singleton3 instance = new Singleton3();

    private Singleton3(){

    }

    public static Singleton3 getInstance() {
        return instance;
    }

    public void sysout() {
        System.out.println("当前实例对象:" + instance);
    }
}
