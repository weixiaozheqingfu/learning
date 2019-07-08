package com.glitter.spring.boot;


import com.glitter.spring.boot.factory.StrategyFactory;
import com.glitter.spring.boot.service.CouponService;
import com.glitter.spring.boot.service.PriceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    @Autowired
    StrategyFactory strategyFactory;

    @Test
    public void strategyTest() {
//        String memberType = "primaryMember";
//        String memberType = "intermediateMember";
        String memberType = "advancedMember";

        PriceService priceService = strategyFactory.getPriceServiceInstance(memberType);
        double booksPrice = priceService.getBooksPrice(100);
        System.out.println("图书价格是:" + booksPrice);

        // 价格Action,计算某会员在双十一的优惠券,并返回给页面客户端调用方
        CouponService couponService = strategyFactory.getCouponServiceInstance(memberType);
        List<String> coupons = couponService.getCoupons("1");
        System.out.println("优惠券是:" + coupons.toString());
    }

}
