package com.glitter.spring.boot;


import com.glitter.spring.boot.factory.MemberStrategyFactory;
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
    PriceService priceService;
    @Autowired
    CouponService couponService;

    @Autowired
    MemberStrategyFactory memberStrategyFactory;

    @Test
    public void strategyTest() {
//        String memberType = "primaryMember";
//        String memberType = "intermediateMember";
        String memberType = "advancedMember";

        priceService.setMemberStrategyService(memberStrategyFactory.getMemberStrategyServiceInstance(memberType));
        double booksPrice = priceService.getBooksPrice(100);
        System.out.println("图书价格是:" + booksPrice);

        // 价格Action,计算某会员在双十一的优惠券,并返回给页面客户端调用方
        couponService.setMemberStrategyService(memberStrategyFactory.getMemberStrategyServiceInstance(memberType));
        List<String> coupons = couponService.getCoupons("1");
        System.out.println("优惠券是:" + coupons.toString());
    }

}
