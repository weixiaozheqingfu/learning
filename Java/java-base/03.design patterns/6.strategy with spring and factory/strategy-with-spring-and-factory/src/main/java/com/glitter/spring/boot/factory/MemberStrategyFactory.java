package com.glitter.spring.boot.factory;

import com.glitter.spring.boot.strategy.MemberStrategyService;

public interface MemberStrategyFactory{

    MemberStrategyService getMemberStrategyServiceInstance(String memberType);

}
