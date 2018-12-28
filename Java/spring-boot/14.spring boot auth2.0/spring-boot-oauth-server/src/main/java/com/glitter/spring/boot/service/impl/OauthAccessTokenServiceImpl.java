package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.AccessTokenInner;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class OauthAccessTokenServiceImpl implements IOauthAccessTokenService{

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenServiceImpl.class);

    @Autowired
    IOauthAccessTokenDao oauthAccessTokenDao;

    /**
     * 验证accessToken
     * @param accessToken
     * @return
     */
    @Override
    public AccessTokenInner validateAccessToken(String accessToken) {
        // 1.验证accessToken是否存在
        OauthAccessToken record = new OauthAccessToken();
        record.setAccessToken(accessToken);
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenDao.get(record);
        if (null == oauthAccessTokenDb) {
            throw new BusinessException("50035", "accessToken不存在");
        }

        // 2.验证accessToken是否在有效期内
        if(System.currentTimeMillis() > oauthAccessTokenDb.getAccessTokenExpireTime().getTime()){
            throw new BusinessException("50036", "accessToken失效");
        }

        // 3.返回当前请求AccessToken的权限相关数据
        AccessTokenInner accessTokenInner = new AccessTokenInner();
        accessTokenInner.setClientId(oauthAccessTokenDb.getClientId());
        accessTokenInner.setUserId(oauthAccessTokenDb.getUserId());
        accessTokenInner.setOpenId(oauthAccessTokenDb.getOpenId());
        accessTokenInner.setUnionId(oauthAccessTokenDb.getUnionId());
        accessTokenInner.setInterfaceUri(Arrays.asList(StringUtils.join(oauthAccessTokenDb.getInterfaceUri(),",")));

        return accessTokenInner;
    }

    /**
     * 根据主键删除accessToken
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthAccessToken record = new OauthAccessToken();
        record.setId(id);
        record.setDeleteFlag(true);
        record.setUpdateTime(new Date());
        int count = oauthAccessTokenDao.updateById(record);
        if(count < 1){
            logger.error("OauthAccessTokenServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }



}