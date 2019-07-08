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
