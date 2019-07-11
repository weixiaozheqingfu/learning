package com.glitter.spring.boot;


import com.glitter.spring.boot.adapter.TypecAdapterHelper;
import com.glitter.spring.boot.adapter.TypecAdapterService;
import com.glitter.spring.boot.service.HdmiService;
import com.glitter.spring.boot.service.MicroUsbService;
import com.glitter.spring.boot.service.MobileTypecService;
import com.glitter.spring.boot.service.VgaService;
import com.glitter.spring.boot.service.impl.HdmiServiceImpl;
import com.glitter.spring.boot.service.impl.MicroUsbServiceImpl;
import com.glitter.spring.boot.service.impl.MobileTypecServiceImpl;
import com.glitter.spring.boot.service.impl.VgaServiceImpl;
import com.glitter.spring.boot.util.ApplicationContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 适配器模式的运用场景：
 *
 * 1.项目运行维护后期,增加了其他的方式的接口和类,与现有接口不兼容,又想统一使用现有接口,可以考虑使用适配器模式接入进来。
 *
 * 2.业务分析后,发现适合使用适配器模式进行架构,比如spring的HandlerAdapter。
 *   满足一种功能场景，可能有不同的实现方式，并且各个实现方式之间的差异还比较大，但是他们又都是在为同一个功能服务，这时候，就可以考虑适配器模式了。
 *   适配器模式可以统一调用方式，又会具体指向真正适配那一种方式进行具体操作。
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdapterTest {

    @Autowired
    TypecAdapterHelper typecAdapterHelper;
    @Autowired
    ApplicationContextHolder applicationContextHolder;

    @Test
    public void HdmiAdapterTest() {
        // 1.获取适配器最终要对接适配的接口实例对象
        Object handler = this.getHandler("vga");
        // 2.获取handler实例对象对应的适配器(那根细线)
        TypecAdapterService typecAdapterService = typecAdapterHelper.getTypecAdapterService(handler);
        // 3.调用typecAdapterService标准接口进行业务处理
        typecAdapterService.input("你好，适配器模式!", handler);
    }

    /**
     * 根据业务场景的不同得到对应的这种场景下需要做实际工作的对象。
     *
     * 这里只是以param.equals("hdmi")的方式简单匹配，实际业务场景可能会根据更复杂的条件来做出选择。
     *
     * @param param
     * @return
     */
    private Object getHandler(String param) {
        if (param.equals("hdmi")) {
            HdmiService hdmiService = applicationContextHolder.getBean(HdmiServiceImpl.class);
            return hdmiService;
        }
        if (param.equals("microUsb")) {
            MicroUsbService microUsbService = applicationContextHolder.getBean(MicroUsbServiceImpl.class);
            return microUsbService;
        }
        if (param.equals("mobileTypec")) {
            MobileTypecService mobileTypecService = applicationContextHolder.getBean(MobileTypecServiceImpl.class);
            return mobileTypecService;
        }
        if (param.equals("vga")) {
            VgaService vgaService = applicationContextHolder.getBean(VgaServiceImpl.class);
            return vgaService;
        }
        return null;
    }

}
