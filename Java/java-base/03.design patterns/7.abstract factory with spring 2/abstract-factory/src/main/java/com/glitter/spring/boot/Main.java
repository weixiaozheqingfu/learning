package com.glitter.spring.boot;

import com.glitter.spring.boot.factory.PcServiceAbstractFactory;
import com.glitter.spring.boot.factory.PcServiceFactoryProducer;
import com.glitter.spring.boot.service.KeyboardService;
import com.glitter.spring.boot.service.MouseService;


/**
 * 厂商可以扩展,比如再扩展一个厂商小米
 * 每个厂商配套可以生产的设备服务也可以扩展,比如都在扩展一个显示器
 *
 * 抽象工厂模式的一个非常大的好处就是,他可以将一系列接口打包,外部只要指定一个入口参数即可,
 * 比如外部指定是hp系列,那么好,代码中会自动根据传入的参数找到对应的hp工厂,进而,所有的pc接口(鼠标接口,键盘接口,屏幕接口)都会自动使用hp系列的。
 * 比如外部指定是dell系列,那么好,代码中会自动根据传入的参数找到对应的dell工厂,进而,所有的pc接口(鼠标接口,键盘接口,屏幕接口)都会自动使用dell系列的。
 * 比如外部指定是huawei系列,那么好,代码中会自动根据传入的参数找到对应的huawei工厂,进而,所有的pc接口(鼠标接口,键盘接口,屏幕接口)都会自动使用huawei系列的。
 * 比如外部指定是xiaomi系列,那么好,代码中会自动根据传入的参数找到对应的xiaomi工厂,进而,所有的pc接口(鼠标接口,键盘接口,屏幕接口)都会自动使用xiaomi系列的。
 *
 * 所以抽象工厂更适合解决更负责的接口装配工作,他是针对某项业务非常负责,而这项业务又有很多实现方,每个实现方又都会有自己一揽子的接口类(当然不同实现方的接口类和接口方法要设计一致,不同方,只是内部实现方式不同)
 * 此时,抽象工厂模式就可以非常方便的自动装配某一个实现方的所有接口实现类.
 */
public class Main {

    public static void main(String[] args){
        // PcServiceFactoryProducer可以设计为单例模式,或者getPcServiceFactory方法设置为static静态方法,会更好
        PcServiceFactoryProducer pcServiceFactoryProducer = new PcServiceFactoryProducer();

        PcServiceAbstractFactory hpPcServiceFactory = pcServiceFactoryProducer.getPcServiceFactory("hp");
        KeyboardService hpKeyboardService = hpPcServiceFactory.getKeyboardServiceInstance();
        hpKeyboardService.input("写字");
        MouseService hpMouseService = hpPcServiceFactory.getMouseServiceInstance();
        hpMouseService.click();

        PcServiceAbstractFactory dellPcServiceFactory = pcServiceFactoryProducer.getPcServiceFactory("dell");
        KeyboardService dellKeyboardService = dellPcServiceFactory.getKeyboardServiceInstance();
        dellKeyboardService.input("写字");
        MouseService dellMouseService = dellPcServiceFactory.getMouseServiceInstance();
        dellMouseService.click();

        PcServiceAbstractFactory huaweiPcServiceFactory = pcServiceFactoryProducer.getPcServiceFactory("huawei");
        KeyboardService huaweiKeyboardService = huaweiPcServiceFactory.getKeyboardServiceInstance();
        huaweiKeyboardService.input("写字");
        MouseService huaweiMouseService = huaweiPcServiceFactory.getMouseServiceInstance();
        huaweiMouseService.click();

    }

}
