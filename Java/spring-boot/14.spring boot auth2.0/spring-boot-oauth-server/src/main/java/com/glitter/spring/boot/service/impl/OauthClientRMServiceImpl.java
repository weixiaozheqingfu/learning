package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthClientRMService;
import com.glitter.spring.boot.bean.OauthClientRM;
import com.glitter.spring.boot.persistence.dao.IOauthClientRMDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OauthClientRMServiceImpl implements IOauthClientRMService{

    private static final Logger logger = LoggerFactory.getLogger(OauthClientRMServiceImpl.class);

    @Autowired
    IOauthClientRMDao oauthClientRMDao;

    /**
     * 创建客户端的资源信息映射
     *
     * @param clientId
     * @param userId
     * @return
     */
    @Override
    public String create(String clientId, Long userId) {
        if(StringUtils.isBlank(clientId)){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数异常,clientId为空");
        }
        if(null == userId){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数异常,userId为空");
        }

        OauthClientRM oauthClientRM = new OauthClientRM();
        String openId = "o_" + UUID.randomUUID().toString().replace("-","");
        Date now = new Date();
        oauthClientRM.setCreateTime(now);
        oauthClientRM.setUpdateTime(now);
        oauthClientRM.setOpenId(openId);
        oauthClientRM.setClientId(clientId);
        oauthClientRM.setUserId(userId);
        oauthClientRMDao.insert(oauthClientRM);
        return openId;
    }

    /**
     * 修改客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param oauthClientRM
     */
    @Override
    public void modifyById(OauthClientRM oauthClientRM) {
        if(null == oauthClientRM){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthClientRM.setUpdateTime(new Date());
        oauthClientRMDao.updateById(oauthClientRM);
    }

    /**
     * 根据主键删除客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthClientRM record = new OauthClientRM();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthClientRMDao.updateById(record);
        if(count < 1){
            logger.error("OauthClientRMServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthClientRM getById(Long id) {
        OauthClientRM result = null;
        if(null == id){
            return result;
        }
        result = oauthClientRMDao.getOauthClientRMById(id);
        return result;
    }

    @Override
    public OauthClientRM getByClientIdAndUserId(String clientId, Long userId) {
        OauthClientRM result = null;
        if(StringUtils.isBlank(clientId) || null == userId){
            return result;
        }
        OauthClientRM recordOauthClientRM = new OauthClientRM();
        recordOauthClientRM.setClientId(clientId);
        recordOauthClientRM.setUserId(userId);
        result = oauthClientRMDao.getOauthClientRM(recordOauthClientRM);
        return result;
    }
}