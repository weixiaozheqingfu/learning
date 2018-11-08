package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IAuthClientInfoService;
import com.glitter.spring.boot.bean.AuthClientInfo;
import com.glitter.spring.boot.persistence.dao.IAuthClientInfoDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthClientInfoServiceImpl implements IAuthClientInfoService{

    private static final Logger logger = LoggerFactory.getLogger(AuthClientInfoServiceImpl.class);

    @Autowired
    IAuthClientInfoDao authClientInfoDao;

    /**
     * 创建auth客户端信息表; InnoDB free: 488448 kB
     * @param authClientInfo
     */
    @Override
    public void create(AuthClientInfo authClientInfo) {
        if(null == authClientInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        authClientInfo.setCreateTime(now);
        authClientInfo.setUpdateTime(now);
        authClientInfoDao.insert(authClientInfo);
    }

    /**
     * 修改auth客户端信息表; InnoDB free: 488448 kB
     * @param authClientInfo
     */
    @Override
    public void modifyById(AuthClientInfo authClientInfo) {
        if(null == authClientInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        authClientInfo.setUpdateTime(new Date());
        authClientInfoDao.updateById(authClientInfo);
    }

    /**
     * 根据主键删除auth客户端信息表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        AuthClientInfo record = new AuthClientInfo();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = authClientInfoDao.updateById(record);
        if(count < 1){
            logger.error("AuthClientInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取auth客户端信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public AuthClientInfo getAuthClientInfoById(Long id) {
        AuthClientInfo result = null;
        if(null == id){
            return result;
        }
        result = authClientInfoDao.getById(id);
        return result;
    }

}