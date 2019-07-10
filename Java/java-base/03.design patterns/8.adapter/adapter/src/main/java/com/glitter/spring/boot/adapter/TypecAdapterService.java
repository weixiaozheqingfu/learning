package com.glitter.spring.boot.adapter;

/**
 * 电脑针对外设提供了typec接口,外部设备与电脑交互数据就必须通过标准的typec接口的数据线进行数据传输。
 * 代码的上下文环境就是电脑，TypecService接口就是数据线电脑端的标准接口，代码中要与外界交互，就通过TypecService接口进行。
 * 进行数据传输。
 *
 * 而各个适配器实现类其实实现了TypecService接口的实现类，是"实实在在"的一根数据线，这根线的另外一头连接了其他适配的接口。
 * 适配器实现类一头实现了标准的TypecService接口,另外一头注入了其他适配接口，在方法内部可以调用其他适配接口的方法，这样就达到了适配的效果。
 */
public interface TypecAdapterService {

    /**
     * 可以将外设的数据传输给电脑
     */
    void input(String msg);

    /**
     * 可以电脑数据传输给外设
     */
    void output(String msg);

}
