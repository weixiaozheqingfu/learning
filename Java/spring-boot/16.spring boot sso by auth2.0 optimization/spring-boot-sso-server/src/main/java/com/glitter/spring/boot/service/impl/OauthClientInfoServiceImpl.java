package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.persistence.dao.IOauthClientInfoDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthClientInfoServiceImpl implements IOauthClientInfoService{

    private static final Logger logger = LoggerFactory.getLogger(OauthClientInfoServiceImpl.class);

    @Autowired
    IOauthClientInfoDao oauthClientInfoDao;

    /**
     * 创建客户端信息表; InnoDB free: 488448 kB
     * @param oauthClientInfo
     */
    @Override
    public void create(OauthClientInfo oauthClientInfo) {
        if(null == oauthClientInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthClientInfo.setCreateTime(now);
        oauthClientInfo.setUpdateTime(now);
        oauthClientInfoDao.insert(oauthClientInfo);
    }

    /**
     * 修改客户端信息表; InnoDB free: 488448 kB
     * @param oauthClientInfo
     */
    @Override
    public void modifyById(OauthClientInfo oauthClientInfo) {
        if(null == oauthClientInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthClientInfo.setUpdateTime(new Date());
        oauthClientInfoDao.updateById(oauthClientInfo);
    }

    /**
     * 根据主键删除客户端信息表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthClientInfo record = new OauthClientInfo();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthClientInfoDao.updateById(record);
        if(count < 1){
            logger.error("OauthClientInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取客户端信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthClientInfo getOauthClientInfoById(Long id) {
        OauthClientInfo result = null;
        if(null == id){
            return result;
        }
        result = oauthClientInfoDao.getOauthClientInfoById(id);
        return result;
    }

    @Override
    public OauthClientInfo getOauthClientInfoByClientId(String clientId) {
        OauthClientInfo result = null;
        if(null == clientId){
            return result;
        }
        result = oauthClientInfoDao.getOauthClientInfoByClientId(clientId);
        return result;
    }
}