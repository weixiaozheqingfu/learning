package com.glitter.spring.boot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.AccessTokenOuter;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.service.IAccessToken4RefreshTokenService;
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
public class AccessToken4RefreshTokenServiceImpl implements IAccessToken4RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AccessToken4RefreshTokenServiceImpl.class);

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
     * @param refresh_token
     * @param grant_type
     */
    @Override
    public AccessTokenOuter getAccessTokenInfo(String client_id, String refresh_token, String grant_type) {
        AccessTokenOuter accessTokenInfo = new AccessTokenOuter();
        // 1.参数合法性校验
        // 具体返回什么码,以及前置拦截器配合http状态码返回,需要后续继续完善,这里先做一个简化示例,先实现一版再说优化.
        if (StringUtils.isBlank(client_id)) {
            throw new BusinessException("50029", "clientId参数为空");
        }
        if (StringUtils.isBlank(refresh_token)) {
            throw new BusinessException("50030", "redirect_uri参数为空");
        }
        if (StringUtils.isBlank(grant_type)) {
            throw new BusinessException("50031", "grant_type参数为空");
        }
        if (!GlitterConstants.OAUTH_GRANT_TYPE_REFRESH_TOKEN.equals(grant_type)) {
            throw new BusinessException("50032", "grant_type参数非法");
        }

        // 2.验证客户端身份
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if(null == oauthClientInfo){
            throw new BusinessException("50034", "客户端非法");
        }

        // 3.验证客户端持有的refresh_token是否存在
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenDao.getByRefreshToken(refresh_token);
        logger.info("AccessToken4AuthorizationCodeServiceImpl.getByRefreshToken方法,oauthAccessToken对象:{}", JSONObject.toJSONString(oauthAccessTokenDb));
        if(null == oauthAccessTokenDb || !client_id.equals(oauthAccessTokenDb.getClientId())){
            throw new BusinessException("50035", "refeshToken不存在");
        }

        // 4.验证客户端持有的refresh_token是否过期
        if(System.currentTimeMillis() > oauthAccessTokenDb.getRefreshTokenExpireTime().getTime()){
            throw new BusinessException("50036", "refeshToken码已过期");
        }

        // 5.refeshToken换取accessToken,更新accessToken记录
        Date now = new Date();
        OauthAccessToken oauthAccessToken = new OauthAccessToken();
        oauthAccessToken.setId(oauthAccessTokenDb.getId());
        oauthAccessToken.setAccessToken(UUID.randomUUID().toString().replace("-",""));
        oauthAccessToken.setAccessTokenExpireIn(60L);
        oauthAccessToken.setAccessTokenExpireTime(new Date(now.getTime() + oauthAccessToken.getAccessTokenExpireIn() * 1000L));
        oauthAccessToken.setRefreshToken(UUID.randomUUID().toString().replace("-",""));
        oauthAccessToken.setUpdateTime(now);
        oauthAccessTokenDao.updateById(oauthAccessToken);

        // 6.封装返回数据
        accessTokenInfo.setAccess_token(oauthAccessToken.getAccessToken());
        accessTokenInfo.setScope(oauthAccessToken.getScope());
        accessTokenInfo.setExpires_in(oauthAccessToken.getAccessTokenExpireIn());
        accessTokenInfo.setRefresh_token(oauthAccessToken.getRefreshToken());
        accessTokenInfo.setToken_type(oauthAccessToken.getTokenType());

        // 记录日志 很重要 方便问题追溯
        logger.info("AccessToken4RefreshTokenServiceImpl.getAccessTokenInfo方法,oauthAccessTokenDb对象:{},accessTokenInfo对象:{}", JSONObject.toJSONString(oauthAccessTokenDb),JSONObject.toJSONString(accessTokenInfo));
        return accessTokenInfo;
    }

}