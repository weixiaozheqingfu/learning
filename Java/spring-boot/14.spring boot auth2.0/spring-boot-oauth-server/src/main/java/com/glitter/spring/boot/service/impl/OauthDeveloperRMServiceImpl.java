package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthDeveloperRMService;
import com.glitter.spring.boot.bean.OauthDeveloperRM;
import com.glitter.spring.boot.persistence.dao.IOauthDeveloperRMDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthDeveloperRMServiceImpl implements IOauthDeveloperRMService{

    private static final Logger logger = LoggerFactory.getLogger(OauthDeveloperRMServiceImpl.class);

    @Autowired
    IOauthDeveloperRMDao oauthDeveloperRMDao;

    /**
     * 创建开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param oauthDeveloperRM
     */
    @Override
    public void create(OauthDeveloperRM oauthDeveloperRM) {
        if(null == oauthDeveloperRM){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthDeveloperRM.setCreateTime(now);
        oauthDeveloperRM.setUpdateTime(now);
        oauthDeveloperRMDao.insert(oauthDeveloperRM);
    }

    /**
     * 修改开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param oauthDeveloperRM
     */
    @Override
    public void modifyById(OauthDeveloperRM oauthDeveloperRM) {
        if(null == oauthDeveloperRM){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthDeveloperRM.setUpdateTime(new Date());
        oauthDeveloperRMDao.updateById(oauthDeveloperRM);
    }

    /**
     * 根据主键删除开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthDeveloperRM record = new OauthDeveloperRM();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthDeveloperRMDao.updateById(record);
        if(count < 1){
            logger.error("OauthDeveloperRMServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthDeveloperRM getOauthDeveloperRMById(Long id) {
        OauthDeveloperRM result = null;
        if(null == id){
            return result;
        }
        result = oauthDeveloperRMDao.getOauthDeveloperRMById(id);
        return result;
    }

}