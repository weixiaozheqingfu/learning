package com.glitter.spring.boot.service;

import com.glitter.spring.boot.strategy.MemberStrategyService;

import java.util.List;

public interface CouponService {

    void setMemberStrategyService(MemberStrategyService memberStrategyService);

    /**
     * 获取双十一活动时的某种优惠券的列表
     *
     * @param couponType 优惠券种类
     * @return 可以获取的优惠券列表(正常优惠券的信息应该是一个对象,里面有很多信息,这里演示为简单就用String代替)
     */
    List<String> getCoupons(String couponType);

    // 其他优惠券相关的方法.

}
