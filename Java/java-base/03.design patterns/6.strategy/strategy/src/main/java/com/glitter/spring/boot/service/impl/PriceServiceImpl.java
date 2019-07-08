package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.MemberStrategyService;
import com.glitter.spring.boot.service.PriceService;

public class PriceServiceImpl implements PriceService {

    private MemberStrategyService memberStrategyService;

    public PriceServiceImpl(MemberStrategyService memberStrategyService) {
        this.memberStrategyService = memberStrategyService;
    }

    /**
     * 计算图书的价格
     *
     * @param booksPrice 图书的原价
     * @return 计算出打折后的价格
     */
    @Override
    public double getBooksPrice(double booksPrice) {
        // ...
        return this.memberStrategyService.calcPrice(booksPrice);
    }

    // 其他方法

}
