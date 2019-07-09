package com.glitter.spring.boot;


import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.singleton.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Constructor;


/**
 * 单例模式研究我们归研究,但实际项目中使用这种标准的单例模式，然后由自己来创建对象其实较少,因为我们项目中一般使用spring框架,spring下创建的bean默认本来就是单例的.
 * 虽然spring的单例和我们单例模式中的单例不能比,我们的单例模式最终保证,这个单例在整个系统中只能有一个,而spring不能，
 * spring能做的是，你用我管理的bean，我能保证在我spring中给你维护的是一个单例，但是这个对象如果你自行去new的话，那我spring管不着。
 *
 * 但是这也足够我们用了，研究单例模式其实就是提供了一种思想，或一种思路，目的本质上主要是节省系统资源的一种策略，
 * spring将我们的对象都统一维护在他的一个静态map中，目的已经达到了，你自己非要在外部再创建，属于自己搞破坏，代码都是自己写的，
 * 自己不按照约定来，非要特立独行，那也没什么好说的。
 *
 * 随着时代的发展，和spring的广泛使用，单例模式思想很好，却也在时代的变革中产生了很多其他形式，spring就是一种很好的形式，
 * 虽然它不是走标准规范的单例模式，也不可能走，但是也在一定程度上达到了单例的目的。
 *
 * 知道就好了，怎么用，怎么选择，看需要，灵活选择即可，切不可思维僵化，知其本质后方要更灵活，不能为了单例模式而单例，要利用它，不能为其所困。
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SingletonTest {

    @Test
    public void singletonTest() {
        Singleton1 single1 = Singleton1.getInstance();
        Singleton1 single11 = Singleton1.getInstance();
        single1.sysout();
        single11.sysout();

        Singleton2 single2 = Singleton2.getInstance();
        Singleton2 single22 = Singleton2.getInstance();
        single2.sysout();
        single22.sysout();

        Singleton3 single3 = Singleton3.getInstance();
        Singleton3 single33 = Singleton3.getInstance();
        single3.sysout();
        single33.sysout();

        Singleton4 single4 = Singleton4.getInstance();
        Singleton4 single44 = Singleton4.getInstance();
        single4.sysout();
        single44.sysout();

        Singleton5 single5 = Singleton5.getInstance();
        Singleton5 single55 = Singleton5.getInstance();
        single5.sysout();
        single55.sysout();

        Singleton6 single6 = Singleton6.INSTANCE;
        Singleton6 single66 = Singleton6.INSTANCE;
        single6.sysout();
        single66.sysout();
    }


    @Test
    public void singleton4Test() throws Exception {
        Class clazz = Class.forName("com.glitter.spring.boot.singleton.Singleton4");
        Constructor c = clazz.getDeclaredConstructor(null);

        c.setAccessible(true);

        Singleton4 s1 = (Singleton4) c.newInstance();
        Singleton4 s2 = (Singleton4) c.newInstance();
        //通过反射，得到的两个不同对象
        System.out.println(s1);
        System.out.println(s2);
    }

    @Test
    public void singleton7Test() throws Exception {
        Class clazz = Class.forName("com.glitter.spring.boot.singleton.Singleton7");
        Constructor c = clazz.getDeclaredConstructor(null);

        c.setAccessible(true);

        //通过反射，得到的两个不同对象
        Singleton7 single7 = Singleton7.getInstance();
        single7.sysout();
        Singleton7 s1 = (Singleton7) c.newInstance();
        System.out.println(s1);
        Singleton7 single77 = Singleton7.getInstance();
        single77.sysout();


        Singleton7 s2 = (Singleton7) c.newInstance();
        System.out.println(s2);
    }

    @Test
    public void registerSingletonTest() throws Exception {
        UserInfo userInfo8 = RegisterSingleton.getInstance("com.glitter.spring.boot.bean.UserInfo");
        UserInfo userInfo88 = RegisterSingleton.getInstance("com.glitter.spring.boot.bean.UserInfo");
        UserInfo userInfo888 = RegisterSingleton.getInstance(UserInfo.class);
        UserInfo userInfo8888 = RegisterSingleton.getInstance(UserInfo.class);
        System.out.println(userInfo8);
        System.out.println(userInfo88);
        System.out.println(userInfo888);
        System.out.println(userInfo8888);
    }

}
