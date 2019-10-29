package com.glitter.spring.boot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.AccessTokenOuter;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.OauthCode;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.service.IAccessToken4AuthorizationCodeService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.IOauthCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AccessToken4AuthorizationCodeServiceImpl implements IAccessToken4AuthorizationCodeService {

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
    public AccessTokenOuter getAccessTokenInfo(String client_id, String client_secret, String redirect_uri, String code, String grant_type) {
        AccessTokenOuter accessTokenInfo = new AccessTokenOuter();
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
        if (!GlitterConstants.OAUTH_GRANT_TYPE_AUTHORIZATION_CODE.equals(grant_type)) {
            throw new BusinessException("50032", "grant_type参数非法");
        }

        // 2.验证客户端身份
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if(null == oauthClientInfo || !client_secret.equals(oauthClientInfo.getClientSecret())){
            throw new BusinessException("40034", "客户端非法");
        }

        // 3.验证客户端持有的code码是否存在
        OauthCode oauthCode = oauthCodeService.getOauthCodeByCode(code);
        logger.info("AccessToken4AuthorizationCodeServiceImpl.getAccessTokenInfo方法,oauthCode对象:{}", JSONObject.toJSONString(oauthCode));
        if(null == oauthCode || !client_id.equals(oauthCode.getClientId())){
            throw new BusinessException("40035", "code码不存在");
        }

        // 4.验证客户端持有的code码是否过期
        if(System.currentTimeMillis() > oauthCode.getExpireTime().getTime()){
            throw new BusinessException("40036", "code码已过期");
        }

        // 验证用户对该客户端是否存在授权记录,如果存在记录,并且refresh_token未过期,则进行更新操作
        // 如果不存在记录,或者存在记录,但refresh_token已过期,则进行新增操作
        // 对于更新的情况,其实先删后增也可以,或者更好,干净利索,因为既然是有新的合法的code值来换取accessToken了,就说明用户又重新授权了一次,之前的当然应该删除掉
        // 这样来看,就更简单了,只要存在记录就直接删掉就好,不管是refresh_token已过期记录还是未过期记录
        OauthAccessToken record = new OauthAccessToken();
        record.setJsessionId(oauthCode.getJsessionId());
        record.setClientId(oauthCode.getClientId());
        record.setUserId(oauthCode.getUserId());
        List<OauthAccessToken> oauthAccessTokensDb = oauthAccessTokenDao.findList(record);
        if (null != oauthAccessTokensDb && oauthAccessTokensDb.size() >0) {
            for (int i = 0; i < oauthAccessTokensDb.size(); i++) {
                oauthAccessTokenDao.deleteById(oauthAccessTokensDb.get(i).getId());
            }
        }

        // 5.code换取accessToken
        OauthAccessToken oauthAccessToken = new OauthAccessToken();
        Date now = new Date();
        oauthAccessToken.setClientId(oauthCode.getClientId());
        oauthAccessToken.setScope(oauthCode.getScope());
        oauthAccessToken.setInterfaceUri(oauthCode.getInterfaceUri());
        oauthAccessToken.setTokenType("bearer");
        oauthAccessToken.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
        // 此处应取jsessionid剩余过期时间
        oauthAccessToken.setAccessTokenExpireIn(60 * 5L);
        oauthAccessToken.setAccessTokenExpireTime(new Date(now.getTime() + oauthAccessToken.getAccessTokenExpireIn() * 1000L));
        oauthAccessToken.setRefreshToken(UUID.randomUUID().toString().replace("-", ""));
        // 此处应取jsessionid剩余过期时间
        oauthAccessToken.setRefreshTokenExpireIn(60 * 5L);
        oauthAccessToken.setRefreshTokenExpireTime(new Date(now.getTime() + oauthAccessToken.getRefreshTokenExpireIn() * 1000L));
        oauthAccessToken.setDeleteFlag(false);
        oauthAccessToken.setCreateTime(now);
        oauthAccessToken.setUpdateTime(now);
        oauthAccessTokenDao.insert(oauthAccessToken);

        // 6.删除code码
        oauthCodeService.deleteByCode(code);

        // 7.封装返回数据
        accessTokenInfo.setAccess_token(oauthAccessToken.getAccessToken());
        accessTokenInfo.setScope(oauthAccessToken.getScope());
        accessTokenInfo.setExpires_in(oauthAccessToken.getAccessTokenExpireIn());
        accessTokenInfo.setRefresh_token(oauthAccessToken.getRefreshToken());
        accessTokenInfo.setToken_type(oauthAccessToken.getTokenType());

        // 记录日志 很重要 方便问题追溯
        logger.info("AccessToken4AuthorizationCodeServiceImpl.getAccessTokenInfo方法,oauthCode对象:{},accessTokenInfo对象:{}", JSONObject.toJSONString(oauthCode),JSONObject.toJSONString(accessTokenInfo));
        return accessTokenInfo;
    }

}