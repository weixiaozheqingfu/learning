package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OauthAccessTokenServiceImpl implements IOauthAccessTokenService {

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenServiceImpl.class);

    @Autowired
    IOauthAccessTokenDao oauthAccessTokenDao;

    /**
     * 创建access_token
     * @param oauthAccessToken
     */
    @Override
    @Transactional
    public void create(OauthAccessToken oauthAccessToken) {
        if(null == oauthAccessToken){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        Date now = new Date();
        oauthAccessToken.setCreateTime(now);
        oauthAccessToken.setUpdateTime(now);
        oauthAccessTokenDao.insert(oauthAccessToken);
    }

    /**
     * 修改access_token
     * @param oauthAccessToken
     */
    @Override
    @Transactional
    public void modifyById(OauthAccessToken oauthAccessToken) {
        if(null == oauthAccessToken || null == oauthAccessToken.getId()){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        oauthAccessToken.setUpdateTime(new Date());
        oauthAccessTokenDao.updateById(oauthAccessToken);
    }

    /**
     * 根据主键删除access_token
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        oauthAccessTokenDao.deleteById(id);
    }

    @Override
    public void deleteByJsessionid(String jsessionid) {
        OauthAccessToken record = new OauthAccessToken();
        record.setJsessionid(jsessionid);
        oauthAccessTokenDao.delete(record);
    }

    /**
     * 根据主键获取access_token
     * @param id
     * @return
     */
    @Override
    public OauthAccessToken getOauthAccessTokenById(Long id) {
        OauthAccessToken result = null;
        if(null == id){
            return result;
        }
        result = oauthAccessTokenDao.getById(id);
        return result;
    }

    @Override
    public OauthAccessToken getOauthAccessTokenByJsessionid(String jsessionid) {
        OauthAccessToken result = null;
        if(null == jsessionid){
            return result;
        }
        OauthAccessToken record = new OauthAccessToken();
        record.setJsessionid(jsessionid);
        result = oauthAccessTokenDao.getOauthAccessToken(record);
        return result;
    }

    @Override
    public OauthAccessToken getOauthAccessTokenByAccessToken(String access_token) {
        OauthAccessToken result = null;
        if(null == access_token){
            return result;
        }
        OauthAccessToken record = new OauthAccessToken();
        record.setAccessToken(access_token);
        result = oauthAccessTokenDao.getOauthAccessToken(record);
        return result;
    }

}