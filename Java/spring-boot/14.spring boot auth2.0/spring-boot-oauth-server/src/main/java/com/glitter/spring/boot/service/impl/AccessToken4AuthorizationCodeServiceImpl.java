package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.AccessTokenInfo;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.OauthCode;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.service.IAccessTokenService;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.IOauthCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AccessToken4AuthorizationCodeServiceImpl implements IAccessTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AccessToken4AuthorizationCodeServiceImpl.class);

    @Autowired
    private IOauthCodeService oauthCodeService;

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Autowired
    private IOauthAccessTokenDao oauthAccessTokenDao;


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

        // 2.验证客户端身份
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if(null == oauthClientInfo || !client_secret.equals(oauthClientInfo.getClientSecret())){
            throw new BusinessException("40034", "系统异常");
        }

        // 3.验证客户端持有的code码
        OauthCode oauthCode = oauthCodeService.getOauthCodeByCode(code);
        if(null == oauthCode || !client_id.equals(oauthCode.getClientId())){
            throw new BusinessException("40035", "系统异常");
        }

        // 4.code换取accessToken
        Date now = new Date();
        OauthAccessToken oauthAccessToken = new OauthAccessToken();
        oauthAccessToken.setUserId(oauthCode.getUserId());
        oauthAccessToken.setOpenId(oauthCode.getOpenId());
        oauthAccessToken.setUnionId(oauthCode.getUnionId());
        oauthAccessToken.setClientId(oauthCode.getClientId());
        oauthAccessToken.setScope(oauthCode.getScope());
        oauthAccessToken.setInterfaceUri(oauthCode.getInterfaceUri());
        oauthAccessToken.setTokenType("bearer");
        oauthAccessToken.setAccessToken(UUID.randomUUID().toString().replace("-",""));
        oauthAccessToken.setAccessTokenExpireIn(60L);
        oauthAccessToken.setAccessTokenExpireTime(new Date(now.getTime() + oauthAccessToken.getAccessTokenExpireIn() * 1000L));
        oauthAccessToken.setRefreshToken(UUID.randomUUID().toString().replace("-",""));
        oauthAccessToken.setRefreshTokenExpireIn(60 * 60 * 24 * 2L);
        oauthAccessToken.setRefreshTokenExpireTime(new Date(now.getTime() + oauthAccessToken.getRefreshTokenExpireIn() * 1000L));
        oauthAccessToken.setDeleteFlag(false);
        oauthAccessToken.setCreateTime(now);
        oauthAccessToken.setUpdateTime(now);
        oauthAccessTokenDao.insert(oauthAccessToken);

        // 5.封装返回数据
        accessTokenInfo.setAccess_token(oauthAccessToken.getAccessToken());
        accessTokenInfo.setScope(oauthAccessToken.getScope());
        accessTokenInfo.setExpires_in(oauthAccessToken.getAccessTokenExpireIn());
        accessTokenInfo.setRefresh_token(oauthAccessToken.getRefreshToken());
        accessTokenInfo.setToken_type(oauthAccessToken.getTokenType());
        accessTokenInfo.setOpenid(oauthAccessToken.getOpenId());
        return accessTokenInfo;
    }

}