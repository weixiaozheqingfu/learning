package com.glitter.spring.boot.factory.impl;

import com.glitter.spring.boot.factory.StrategyFactory;
import com.glitter.spring.boot.service.CouponService;
import com.glitter.spring.boot.service.PriceService;
import com.glitter.spring.boot.strategy.MemberStrategyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class StrategyFactoryImpl implements StrategyFactory,InitializingBean {

    @Autowired
    MemberStrategyService primaryMemberStrategyServiceImpl;
    @Autowired
    MemberStrategyService intermediateMemberStrategyServiceImpl;
    @Autowired
    MemberStrategyService advancedMemberStrategyServiceImpl;

    private static final Map<String, MemberStrategyService> map = new HashMap<>();

    @Autowired
    PriceService priceService;
    @Autowired
    CouponService couponService;


    @Override
    public void afterPropertiesSet() throws Exception {
        map.put("primaryMember", primaryMemberStrategyServiceImpl);
        map.put("intermediateMember", intermediateMemberStrategyServiceImpl);
        map.put("advancedMember", advancedMemberStrategyServiceImpl);
    }

    @Override
    public PriceService getPriceServiceInstance(String memberType) {
        MemberStrategyService memberStrategyService = this.getMemberStrategyServiceInstance(memberType);
        if (null == memberStrategyService) {
            throw new RuntimeException("memberStrategyService is null");
        }
        priceService.setMemberStrategyService(memberStrategyService);
        return priceService;
    }

    @Override
    public CouponService getCouponServiceInstance(String memberType) {
        MemberStrategyService memberStrategyService = this.getMemberStrategyServiceInstance(memberType);
        if (null == memberStrategyService) {
            throw new RuntimeException("memberStrategyService is null");
        }
        couponService.setMemberStrategyService(memberStrategyService);
        return couponService;
    }

    private MemberStrategyService getMemberStrategyServiceInstance(String memberType) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        if (StringUtils.isBlank(memberType)) {
            return null;
        }
        return map.get(memberType);
    }
}
