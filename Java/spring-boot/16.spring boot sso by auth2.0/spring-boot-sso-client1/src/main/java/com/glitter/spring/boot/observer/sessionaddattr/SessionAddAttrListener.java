package com.glitter.spring.boot.observer.sessionaddattr;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.ISession;
import com.glitter.spring.boot.web.action.UserInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SessionAddAttrListener implements ApplicationListener<SessionAddAttrEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(SessionAddAttrEvent applicationEvent) {
        logger.info("SessionAddAttrListener.onApplicationEvent,输入参数:{}", applicationEvent);
        SessionAddAttrParam sessionAddAttrParam = applicationEvent.getContent();
        ISession session = sessionAddAttrParam.getSession();
        Map<String, Object> attribute = sessionAddAttrParam.getAttribute();
        logger.info("SessionAddAttrListener.onApplicationEvent,sessionId:{}", session.getId());

        // 限制多端同时登陆逻辑
        UserInfo userInfo = null;
        if(null == (userInfo = (UserInfo) attribute.get(GlitterConstants.SESSION_USER))){ return; }
        commonCache.add(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getUserId())), session.getId(), cacheKeyManager.getLimitMultiLoginKeyExpireTime());
        return;
    }

}
