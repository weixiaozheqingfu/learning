package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthAccessTokenServiceImpl implements IOauthAccessTokenService{

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenServiceImpl.class);

    @Autowired
    IOauthAccessTokenDao oauthAccessTokenDao;

    /**
     * 创建accessToken表; InnoDB free: 488448 kB
     * @param oauthAccessToken
     */
    @Override
    public void create(OauthAccessToken oauthAccessToken) {
        if(null == oauthAccessToken){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthAccessToken.setCreateTime(now);
        oauthAccessToken.setUpdateTime(now);
        oauthAccessToken.setDeleteFlag(false);
        oauthAccessTokenDao.insert(oauthAccessToken);
    }

    /**
     * 修改accessToken表
     * @param oauthAccessToken
     */
    @Override
    public void modifyById(OauthAccessToken oauthAccessToken) {
        if(null == oauthAccessToken){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthAccessToken.setUpdateTime(new Date());
        oauthAccessTokenDao.updateById(oauthAccessToken);
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