package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.CouponService;
import com.glitter.spring.boot.service.MemberStrategyService;

import java.util.List;

public class CouponServiceImpl implements CouponService {

    private MemberStrategyService memberStrategyService;

    public CouponServiceImpl(MemberStrategyService memberStrategyService) {
        this.memberStrategyService = memberStrategyService;
    }

    /**
     * 获取双十一活动时的某种优惠券的列表
     *
     * @param couponType 优惠券种类
     * @return 可以获取的优惠券列表(正常优惠券的信息应该是一个对象, 里面有很多信息, 这里演示为简单就用String代替)
     */
    @Override
    public List<String> getCoupons(String couponType) {
        // ...
        return this.memberStrategyService.calcCoupons(couponType);
    }

    // 其他方法

}
