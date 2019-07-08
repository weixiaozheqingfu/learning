package com.glitter.spring.boot.strategy;

import java.util.List;

/**
 * 凡是涉及到不同的会员级别会有不同计算的情况,都在该接口中定义方法
 */
public interface MemberStrategyService {

    /**
     * 计算图书的价格
     * @param booksPrice    图书的原价
     * @return    计算出打折后的价格
     */
    double calcPrice(double booksPrice);

    /**
     * 计算双十一活动时的某种优惠券的列表
     *
     * @param couponType 优惠券种类
     * @return 计算可以获取的优惠券列表(正常优惠券的信息应该是一个对象,里面有很多信息,这里演示为简单就用String代替)
     */
    List<String> calcCoupons(String couponType);

}
