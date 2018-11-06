package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IAuthTokenInfoService;
import com.glitter.spring.boot.bean.AuthTokenInfo;
import com.glitter.spring.boot.persistence.dao.IAuthTokenInfoDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthTokenInfoServiceImpl implements IAuthTokenInfoService{

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenInfoServiceImpl.class);

    @Autowired
    IAuthTokenInfoDao authTokenInfoDao;

    /**
     * 创建auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param authTokenInfo
     */
    @Override
    public void create(AuthTokenInfo authTokenInfo) {
        if(null == authTokenInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        authTokenInfo.setCreateTime(now);
        authTokenInfo.setUpdateTime(now);
        authTokenInfoDao.insert(authTokenInfo);
    }

    /**
     * 修改auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param authTokenInfo
     */
    @Override
    public void modifyById(AuthTokenInfo authTokenInfo) {
        if(null == authTokenInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        authTokenInfo.setUpdateTime(new Date());
        authTokenInfoDao.updateById(authTokenInfo);
    }

    /**
     * 根据主键删除auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        AuthTokenInfo record = new AuthTokenInfo();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = authTokenInfoDao.updateById(record);
        if(count < 1){
            logger.error("AuthTokenInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public AuthTokenInfo getAuthTokenInfoById(Long id) {
        AuthTokenInfo result = null;
        if(null == id){
            return result;
        }
        result = authTokenInfoDao.getById(id);
        return result;
    }

}