package com.glitter.spring.boot.service;

public interface PriceService {

    /**
     * 计算图书的价格
     *
     * @param booksPrice 图书的原价
     * @return 计算出打折后的价格
     */
    double getBooksPrice(double booksPrice);

    // 其他价格相关的方法

}
