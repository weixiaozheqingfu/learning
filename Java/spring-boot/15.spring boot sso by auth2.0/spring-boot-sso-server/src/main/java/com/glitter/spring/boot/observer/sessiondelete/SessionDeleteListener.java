package com.glitter.spring.boot.observer.sessiondelete;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.ISession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SessionDeleteListener implements ApplicationListener<SessionDeleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SessionDeleteListener.class);

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(SessionDeleteEvent applicationEvent) {
        logger.info("SessionDeleteListener.onApplicationEvent,输入参数:{}", applicationEvent);
        ISession session = applicationEvent.getContent();
        logger.info("SessionDeleteListener.onApplicationEvent,sessionId:{}", session.getId());

        // 注销会话,同时注销限制多端同时登陆的相关代码逻辑
        UserInfo userInfo = null;
        if(null == (userInfo = (UserInfo)session.getAttribute(GlitterConstants.SESSION_USER))){ return; }

        // 检查当前的sessionId与gliter:session:limitMultiLogin:userId:1中的sessionId是否一致,
        // 一致则同步删除gliter:session:limitMultiLogin:userId:1键
        // 不一致说明当前的session是被其他端"挤掉"了,当前的session对象自己删除了也就删除了就好了,
        // gliter:session:limitMultiLogin:userId:1已经关联了其他session对象的id,故不删除
        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())));
        if(session.getId().equals(jsessionIdEffective)){
            commonCache.del(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getId())));
        }
        return;
    }

}
