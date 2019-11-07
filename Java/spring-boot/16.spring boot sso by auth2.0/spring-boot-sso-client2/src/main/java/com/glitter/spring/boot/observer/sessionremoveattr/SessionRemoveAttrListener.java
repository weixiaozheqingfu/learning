package com.glitter.spring.boot.observer.sessionremoveattr;

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
public class SessionRemoveAttrListener implements ApplicationListener<SessionRemoveAttrEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private ICommonCache commonCache;

    @Autowired
    private ICacheKeyManager cacheKeyManager;

    @Override
    public void onApplicationEvent(SessionRemoveAttrEvent applicationEvent) {
        logger.info("SessionRemoveAttrListener.onApplicationEvent,输入参数:{}", applicationEvent);
        SessionRemoveAttrParam sessionRemoveAttrParam = applicationEvent.getContent();
        ISession session = sessionRemoveAttrParam.getSession();
        Map<String, Object> attribute = sessionRemoveAttrParam.getAttribute();
        logger.info("SessionRemoveAttrListener.onApplicationEvent,sessionId:{}", session.getId());

        // 注销会话,同时注销限制多端同时登陆的相关代码逻辑
        UserInfo userInfo = null;
        if(null == (userInfo = (UserInfo)attribute.get(GlitterConstants.SESSION_USER))){ return; }

        // 检查当前的sessionId与gliter:session:limitMultiLogin:userId:1中的sessionId是否一致,
        // 一致则同步删除gliter:session:limitMultiLogin:userId:1键
        // 不一致说明当前的session是已经被其他端"挤掉"了,gliter:session:limitMultiLogin:userId:1已经关联了其他session对象的id,所以不可删除
        String jsessionIdEffective = commonCache.get(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getUserId())));
        if(session.getId().equals(jsessionIdEffective)){
            commonCache.del(cacheKeyManager.getLimitMultiLoginKey(String.valueOf(userInfo.getUserId())));
        }
        return;
    }

}
