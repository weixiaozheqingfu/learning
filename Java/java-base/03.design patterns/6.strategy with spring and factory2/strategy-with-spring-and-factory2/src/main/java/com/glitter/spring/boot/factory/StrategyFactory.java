package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.service.CouponService;
import com.glitter.spring.boot.service.PriceService;

public interface StrategyFactory {

    PriceService getPriceServiceInstance(String memberType);

    CouponService getCouponServiceInstance(String memberType);

}
