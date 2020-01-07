package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IAuthServerInfoService;
import com.glitter.spring.boot.bean.AuthServerInfo;
import com.glitter.spring.boot.persistence.dao.IAuthServerInfoDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServerInfoServiceImpl implements IAuthServerInfoService{

    private static final Logger logger = LoggerFactory.getLogger(AuthServerInfoServiceImpl.class);

    @Autowired
    IAuthServerInfoDao authServerInfoDao;

    /**
     * 创建第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param authServerInfo
     */
    @Override
    public void create(AuthServerInfo authServerInfo) {
        if(null == authServerInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        authServerInfo.setCreateTime(now);
        authServerInfo.setUpdateTime(now);
        authServerInfoDao.insert(authServerInfo);
    }

    /**
     * 修改第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param authServerInfo
     */
    @Override
    public void modifyById(AuthServerInfo authServerInfo) {
        if(null == authServerInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        authServerInfo.setUpdateTime(new Date());
        authServerInfoDao.updateById(authServerInfo);
    }

    /**
     * 根据主键删除第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        AuthServerInfo record = new AuthServerInfo();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = authServerInfoDao.updateById(record);
        if(count < 1){
            logger.error("AuthServerInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public AuthServerInfo getAuthServerInfoById(Long id) {
        AuthServerInfo result = null;
        if(null == id){
            return result;
        }
        result = authServerInfoDao.getById(id);
        return result;
    }

}