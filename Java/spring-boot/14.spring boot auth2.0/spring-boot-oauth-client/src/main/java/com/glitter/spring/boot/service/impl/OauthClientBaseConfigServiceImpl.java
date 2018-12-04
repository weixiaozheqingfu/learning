package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthClientBaseConfigService;
import com.glitter.spring.boot.bean.OauthClientBaseConfig;
import com.glitter.spring.boot.persistence.dao.IOauthClientBaseConfigDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthClientBaseConfigServiceImpl implements IOauthClientBaseConfigService{

    private static final Logger logger = LoggerFactory.getLogger(OauthClientBaseConfigServiceImpl.class);

    @Autowired
    IOauthClientBaseConfigDao oauthClientBaseConfigDao;

    /**
     * 创建auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientBaseConfig
     */
    @Override
    public void create(OauthClientBaseConfig oauthClientBaseConfig) {
        if(null == oauthClientBaseConfig){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthClientBaseConfig.setCreateTime(now);
        oauthClientBaseConfig.setUpdateTime(now);
        oauthClientBaseConfigDao.insert(oauthClientBaseConfig);
    }

    /**
     * 修改auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param oauthClientBaseConfig
     */
    @Override
    public void modifyById(OauthClientBaseConfig oauthClientBaseConfig) {
        if(null == oauthClientBaseConfig){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthClientBaseConfig.setUpdateTime(new Date());
        oauthClientBaseConfigDao.updateById(oauthClientBaseConfig);
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
        OauthClientBaseConfig record = new OauthClientBaseConfig();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthClientBaseConfigDao.updateById(record);
        if(count < 1){
            logger.error("OauthClientBaseConfigServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取auth客户端基本信息配置表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthClientBaseConfig getOauthClientBaseConfigById(Long id) {
        OauthClientBaseConfig result = null;
        if(null == id){
            return result;
        }
        result = oauthClientBaseConfigDao.getOauthClientBaseConfigById(id);
        return result;
    }

}