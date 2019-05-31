package com.glitter.spring.boot.service;

import com.glitter.spring.boot.service.impl.*;

public class Main extends AliPayServiceImpl {

    public static void main(String[] args){

        // 使用接口的方式实现静态代理
        IPayService aliPayService = new AliPayServiceImpl();
        IPayService payService$TimeProxy = new PayService$TimeProxyImpl(aliPayService);
        payService$TimeProxy.pay(10L);

        IPayService chatPayService = new WeChatPayServiceImpl();
        payService$TimeProxy = new PayService$TimeProxyImpl(chatPayService);
        payService$TimeProxy.pay(12L);

        // 使用接口的方式实现静态代理,可以实现多层代理类的叠加
        IPayService aliPayService1 = new AliPayServiceImpl();
        IPayService payService$TimeProxy1 = new PayService$TimeProxyImpl(aliPayService1);
        IPayService payService$LogProxy1 = new PayService$LogProxyImpl(payService$TimeProxy1);
        payService$LogProxy1.pay(10L);

        // 使用接口的方式实现静态代理,可以实现多层代理类的叠加
        IPayService aliPayService2 = new AliPayServiceImpl();
        IPayService payService$LogProxy2 = new PayService$LogProxyImpl(aliPayService2);
        IPayService payService$TimeProxy2 = new PayService$TimeProxyImpl(payService$LogProxy2);
        payService$TimeProxy2.pay(10L);


        // 即便是使用接口方式实现静态代理，静态代理也有其不可克服的缺点。
        // 缺点就是局限性，现在我只有一个IpayService接口,这个接口需要时间和日志的代理，我们可以写两个代理实现类实现IpayService接口。
        // 而我的系统中可能会有很过多个其他的Service接口，他们的实现类需要有时间和日志功能的增强，那怎么办，每一个接口我都要
        // 去写时间和日志的代理类去实现这个接口，这会造成项目中代理类数量的极速膨胀，并且这些代理类的内容都是一模一样的。

        // 既然静态代理有这些缺点无法克服，为什么我们还要学习静态代理呢。
        // 须知只是是有渐进性的，先学习静态代理是为我们后续学习动态代理打下一个基础，在学习动态代理时各方面都容易很多。
        // 再有就是静态代理相对动态代理简单，也不是完全没有使用场景，比如我的代理只是想在某一个service上生效，其他service不生效，那我可以使用静态代理的，没有问题。
    }

}
