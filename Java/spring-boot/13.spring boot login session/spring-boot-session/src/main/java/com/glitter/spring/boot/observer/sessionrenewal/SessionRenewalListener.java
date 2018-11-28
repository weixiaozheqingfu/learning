package com.glitter.spring.boot.observer.sessionrenewal;

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

@Component
public class SessionRenewalListener implements ApplicationListener<SessionRenewalEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(SessionRenewalEvent applicationEvent) {
        logger.info("SessionRenewalListener.onApplicationEvent,输入参数:{}", applicationEvent);
        ISession session = applicationEvent.getContent();
        logger.info("SessionRenewalListener.onApplicationEvent,sessionId:{}", session.getId());
        UserInfo userInfo = null;
        if(null == (userInfo = (UserInfo)session.getAttribute(GlitterConstants.SESSION_USER))){ return; }

        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())));
        if(session.getId().equals(jsessionIdEffective)){
            // 对限制单端登陆进行续期
            if (commonCache.isExists(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())))) {
                commonCache.renewal(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())), cacheKeyManager.getLimitMultiLoginKeyExpireTime());
            }
        }

        return;
    }

}
