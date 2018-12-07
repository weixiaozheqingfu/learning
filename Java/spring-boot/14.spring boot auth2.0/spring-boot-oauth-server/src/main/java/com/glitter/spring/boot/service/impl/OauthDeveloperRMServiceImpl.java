package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.OauthClientRM;
import com.glitter.spring.boot.bean.OauthDeveloperRM;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthDeveloperRMDao;
import com.glitter.spring.boot.service.IOauthDeveloperRMService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OauthDeveloperRMServiceImpl implements IOauthDeveloperRMService {

    private static final Logger logger = LoggerFactory.getLogger(OauthDeveloperRMServiceImpl.class);

    @Autowired
    IOauthDeveloperRMDao oauthDeveloperRMDao;

    /**
     * 创建开放平台开发者账号的资源信息映射
     *
     * @param developerId
     * @param userId
     * @return
     */
    @Override
    public String create(Long developerId, Long userId) {
        if (null == developerId) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,developerId为空");
        }
        if (null == userId) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,userId为空");
        }

        OauthDeveloperRM oauthDeveloperRM = new OauthDeveloperRM();
        String unionId = "u_" + UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        oauthDeveloperRM.setCreateTime(now);
        oauthDeveloperRM.setUpdateTime(now);
        oauthDeveloperRM.setUnionId(unionId);
        oauthDeveloperRM.setDeveloperId(developerId);
        oauthDeveloperRM.setUserId(userId);
        oauthDeveloperRMDao.insert(oauthDeveloperRM);
        return unionId;
    }

    @Override
    public OauthDeveloperRM getByDeveloperIdAndUserId(Long developerId, Long userId) {
        OauthDeveloperRM result = null;
        if(null == developerId || null == userId){
            return result;
        }
        OauthDeveloperRM oauthDeveloperRM = new OauthDeveloperRM();
        oauthDeveloperRM.setDeveloperId(developerId);
        oauthDeveloperRM.setUserId(userId);
        result = oauthDeveloperRMDao.getOauthDeveloperRM(oauthDeveloperRM);
        return result;
    }

    /**
     * 修改开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     *
     * @param oauthDeveloperRM
     */
    @Override
    public void modifyById(OauthDeveloperRM oauthDeveloperRM) {
        if (null == oauthDeveloperRM) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthDeveloperRM.setUpdateTime(new Date());
        oauthDeveloperRMDao.updateById(oauthDeveloperRM);
    }

    /**
     * 根据主键删除开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if (null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthDeveloperRM record = new OauthDeveloperRM();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthDeveloperRMDao.updateById(record);
        if (count < 1) {
            logger.error("OauthDeveloperRMServiceImpl.deleteById方法执行失败,输入参数:{}", id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     *
     * @param id
     * @return
     */
    @Override
    public OauthDeveloperRM getOauthDeveloperRMById(Long id) {
        OauthDeveloperRM result = null;
        if (null == id) {
            return result;
        }
        result = oauthDeveloperRMDao.getOauthDeveloperRMById(id);
        return result;
    }

}