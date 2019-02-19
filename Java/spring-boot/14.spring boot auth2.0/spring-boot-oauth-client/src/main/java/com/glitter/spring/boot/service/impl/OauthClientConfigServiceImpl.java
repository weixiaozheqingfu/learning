package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.OauthClientConfig;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthClientConfigDao;
import com.glitter.spring.boot.service.IOauthClientConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthClientConfigServiceImpl implements IOauthClientConfigService {

    private static final Logger logger = LoggerFactory.getLogger(OauthClientConfigServiceImpl.class);

    @Autowired
    IOauthClientConfigDao oauthClientConfigDao;

    /**
     * 创建auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientConfig
     */
    @Override
    public void create(OauthClientConfig oauthClientConfig) {
        if(null == oauthClientConfig){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthClientConfig.setCreateTime(now);
        oauthClientConfig.setUpdateTime(now);
        oauthClientConfigDao.insert(oauthClientConfig);
    }

    /**
     * 修改auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientConfig
     */
    @Override
    public void modifyById(OauthClientConfig oauthClientConfig) {
        if(null == oauthClientConfig){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthClientConfig.setUpdateTime(new Date());
        oauthClientConfigDao.updateById(oauthClientConfig);
    }

    /**
     * 根据主键删除auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthClientConfig record = new OauthClientConfig();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthClientConfigDao.updateById(record);
        if(count < 1){
            logger.error("OauthClientConfigServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthClientConfig getOauthClientConfigById(Long id) {
        OauthClientConfig result = null;
        if(null == id){
            return result;
        }
        result = oauthClientConfigDao.getById(id);
        return result;
    }

}