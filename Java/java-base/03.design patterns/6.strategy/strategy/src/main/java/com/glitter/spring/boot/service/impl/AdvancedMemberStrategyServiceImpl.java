package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.MemberStrategyService;

import java.util.ArrayList;
import java.util.List;

public class AdvancedMemberStrategyServiceImpl implements MemberStrategyService{

    /**
     * 计算图书的价格
     *
     * @param booksPrice 图书的原价
     * @return 计算出打折后的价格
     */
    @Override
    public double calcPrice(double booksPrice) {
        return booksPrice * 0.8;
    }

    /**
     * 计算双十一活动时的某种优惠券的列表
     *
     * @param couponType 优惠券种类
     * @return 计算可以获取的优惠券列表(正常优惠券的信息应该是一个对象, 里面有很多信息, 这里演示为简单就用String代替)
     */
    @Override
    public List<String> calcCoupons(String couponType) {
        List<String> result = new ArrayList<>();
        result.add("优惠券1");
        result.add("优惠券2");
        return result;
    }

    // 其他方法

}
