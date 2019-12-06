package com.glitter.spring.boot;

        import com.glitter.spring.boot.service.CouponService;
        import com.glitter.spring.boot.service.MemberStrategyService;
        import com.glitter.spring.boot.service.PriceService;
        import com.glitter.spring.boot.service.impl.*;

        import java.util.List;

/**
 * 对比工厂模式和策略模式可以发现
 * 策略模式可以理解为是对工厂模式的进一步封装
 *
 * 策略模式更像是我们传统的实现方式,需要价格相关的service就使用价格相关的service
 * 需要使用优惠券相关的service就使用优惠券相关的service
 * 在priceService中可以有自己的通用逻辑,中间的某一步根据注入的策略类不同而调用不同的策略进行处理.
 *
 * 工厂模式如果直接使用的话,就比较直接了,相当于直接把不同的情况就直接使用不同的service进行处理了,直接暴露给用户的service就是最终的service。
 * 像这里,如果使用工厂模式,那么privateService就会有多个实现类,CouponServcie会有多个实现类,每个的实现类都分为初级,中级,高级三个实现类.
 *
 *
 * 所以你可以看到,策略模式和工厂模式对于这里的处理的聚合方式是不一样的。所要处理的问题和场景也不太一样,没有绝对的好坏,就看你分析完业务后,那种模式的聚合方式更适合你的业务场景就使用哪个即可.
 * 我个人的理解,策略模式解决的是小不同,而工厂模式解决的是大不同
 * 什么是小不同,比如这里的会员,其实都是系统的会员嘛,只是级别不同,而导致其中一部分的算法不同,其他方面会员还有很多相同的属性,这可能就比较适合使用策略模式.公有的逻辑照常放到如priceService的方法中,不同的东西使用注入的策略类处理。
 * 什么是大不同,比如第三方移动支付,可以有微信支付,可以有支付宝支付,反正都要调用他们各自的支付等接口,但是各个接口的处理可能没有任何共性,这时候,就比较合适使用工厂模式,直接对外通过工厂获取不同的实现方进行调用处理即可.
 *
 * 所以到底使用什么模式,还是要具体问题具体分析,分析的场景多了,感觉也就来了,模式会越运用越有感觉,站在前人的肩膀和自己的实践总结上,系统架构总会越来越好.
 *
 * 另外,具体到这里示例,策略模式其实是可以搭配工厂模式使用的,像这里的priceService和couponService的实现类都可以通过工厂模式来拿到.
 *
 * 一般情况下，简单工厂模式可以单独使用，就是一个接口有多个实现类，我用哪一个的问题。
 * 简单工厂模式可以配合抽象工厂模式使用，抽象工厂第一层也是通过简单工厂拿到相关的"系列"工厂，进一步"系列"工厂专门获取自己"系列"下的实现类。
 *
 * 简单工厂模式可以配合策略模式使用。
 * 通常是策略模式包含简单工厂模式，就是一个服务用到哪个策略时，策略类的获取是通过简单工厂模式获取到的。进而将策略类注入服务，服务来调用具体的策略。
 *
 * 简单工厂也可以包含策略模式，当然策略模式中也有策略相关的简单工厂来提供策略实现类。那就成了简单工厂->策略->简单工厂了。
 * 也就是说一个接口有多个实现类，我用哪一个实现类，我用了这个实现类，这个实现类中某些服务需要注入不通的策略，策略的提供又是简单工厂模式提供的。
 * 比如客户端和服务器端接口对接时，可能会传入一个标识位来表明请求数据的数据格式。比如type=1是一种数据格式，type=2是一种数据格式（两种数据格式可能完全不通，内部逻辑可能完全不同）。
 * 并且每一种数据格式根据客户端版本的不同服务器端又会有不同的方式来处理数据返回结果。比如version=1和version=2的处理方式可能是不一样。
 * 那么服务器端可以这样做，第一层使用简单工厂模式根据type来拿到具体对应的实现类，比如type=1就拿到实现类1，实现类1的逻辑比较简单，直接返回一个简单的字符串。
 * type=2就拿到实现类2进行处理，实现类2的逻辑比较复杂，需要进一步根据客户端传的version值的不同来使用不通的策略处理。那么就在实现类2中使用策略模式。
 * 根据version版本de不同，再通过另外一个version的简单工厂拿到具体对应的策略类，注入到type=2的实现类中，type=2的实现类进一步调用对应的策略类方法实现业务逻辑。
 *
 *
 *
 * 还是那句话，具体怎么用，用多了就有感觉了。
 *
 *
 */
public class Main {

    public static void main(String[] args){

//        MemberStrategyService memberStrategyService = new PrimaryMemberStrategyServiceImpl();
//        MemberStrategyService memberStrategyService = new IntermediateMemberStrategyServiceImpl();
        MemberStrategyService memberStrategyService = new AdvancedMemberStrategyServiceImpl();

        // 价格Action,计算某会员的图书价格,并返回给页面客户端调用方
        PriceService priceService = new PriceServiceImpl(memberStrategyService);
        double booksPrice = priceService.getBooksPrice(100);
        System.out.println("图书价格是:" + booksPrice);

        // 价格Action,计算某会员在双十一的优惠券,并返回给页面客户端调用方
        CouponService couponService = new CouponServiceImpl(memberStrategyService);
        List<String> coupons = couponService.getCoupons("1");
        System.out.println("优惠券是:" + coupons.toString());
    }

}
