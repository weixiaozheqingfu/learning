package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.AccessTokenInfo;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAccessTokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccessToken4AuthorizationCodeServiceImpl implements IAccessTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AccessToken4AuthorizationCodeServiceImpl.class);

    /**
     * 获取accessToken信息
     *
     * @param client_id
     * @param client_secret
     * @param redirect_uri
     * @param code
     * @param grant_type
     */
    @Override
    public AccessTokenInfo getAccessTokenInfo(String client_id, String client_secret, String redirect_uri, String code, String grant_type) {
        AccessTokenInfo accessTokenInfo = new AccessTokenInfo();
        // 1.参数合法性校验
        // 具体返回什么码,以及前置拦截器配合http状态码返回,需要后续继续完善,这里先做一个简化示例,先实现一版再说优化.
        if (StringUtils.isBlank(client_id)) {
            throw new BusinessException("40029", "clientId参数为空");
        }
        if (StringUtils.isBlank(client_secret)) {
            throw new BusinessException("40030", "redirect_uri参数为空");
        }
        if (StringUtils.isBlank(redirect_uri)) {
            throw new BusinessException("40031", "redirect_uri参数为空");
        }
        if (StringUtils.isBlank(code)) {
            throw new BusinessException("40032", "code参数为空");
        }
        if (StringUtils.isBlank(grant_type)) {
            throw new BusinessException("40033", "grant_type参数为空");
        }
        accessTokenInfo.setAccess_token("112344");
        accessTokenInfo.setScope("afg");
        accessTokenInfo.setExpires_in(7200);
        accessTokenInfo.setRefresh_token("afg5415af1gas5");
        accessTokenInfo.setToken_type("bearer");
        accessTokenInfo.setOpenid("1");
        return accessTokenInfo;
    }

}