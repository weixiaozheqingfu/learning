package com.glitter.spring.boot.factory.impl;

import com.glitter.spring.boot.factory.MemberStrategyFactory;
import com.glitter.spring.boot.strategy.MemberStrategyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 凡是涉及到不同的会员级别会有不同计算的情况,都在该接口中定义方法
 */
@Component
public class MemberStrategyFactoryImpl implements MemberStrategyFactory,InitializingBean {

    @Autowired
    MemberStrategyService primaryMemberStrategyServiceImpl;
    @Autowired
    MemberStrategyService intermediateMemberStrategyServiceImpl;
    @Autowired
    MemberStrategyService advancedMemberStrategyServiceImpl;

    private static final Map<String, MemberStrategyService> map = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        map.put("primaryMember", primaryMemberStrategyServiceImpl);
        map.put("intermediateMember", intermediateMemberStrategyServiceImpl);
        map.put("advancedMember", advancedMemberStrategyServiceImpl);
    }

    @Override
    public MemberStrategyService getMemberStrategyServiceInstance(String memberType) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        if (StringUtils.isBlank(memberType)) {
            return null;
        }
        return map.get(memberType);
    }

}
