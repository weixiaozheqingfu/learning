package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthClientScenarioConfigService;
import com.glitter.spring.boot.bean.OauthClientScenarioConfig;
import com.glitter.spring.boot.persistence.dao.IOauthClientScenarioConfigDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthClientScenarioConfigServiceImpl implements IOauthClientScenarioConfigService{

    private static final Logger logger = LoggerFactory.getLogger(OauthClientScenarioConfigServiceImpl.class);

    @Autowired
    IOauthClientScenarioConfigDao oauthClientScenarioConfigDao;

    /**
     * 创建auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param oauthClientScenarioConfig
     */
    @Override
    public void create(OauthClientScenarioConfig oauthClientScenarioConfig) {
        if(null == oauthClientScenarioConfig){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthClientScenarioConfig.setCreateTime(now);
        oauthClientScenarioConfig.setUpdateTime(now);
        oauthClientScenarioConfigDao.insert(oauthClientScenarioConfig);
    }

    /**
     * 修改auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param oauthClientScenarioConfig
     */
    @Override
    public void modifyById(OauthClientScenarioConfig oauthClientScenarioConfig) {
        if(null == oauthClientScenarioConfig){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthClientScenarioConfig.setUpdateTime(new Date());
        oauthClientScenarioConfigDao.updateById(oauthClientScenarioConfig);
    }

    /**
     * 根据主键删除auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthClientScenarioConfig record = new OauthClientScenarioConfig();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthClientScenarioConfigDao.updateById(record);
        if(count < 1){
            logger.error("OauthClientScenarioConfigServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取auth客户端业务场景信息配置表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthClientScenarioConfig getOauthClientScenarioConfigById(Long id) {
        OauthClientScenarioConfig result = null;
        if(null == id){
            return result;
        }
        result = oauthClientScenarioConfigDao.getOauthClientScenarioConfigById(id);
        return result;
    }

}