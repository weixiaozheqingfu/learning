package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IAuthServerInterfaceInfoService;
import com.glitter.spring.boot.bean.AuthServerInterfaceInfo;
import com.glitter.spring.boot.persistence.dao.IAuthServerInterfaceInfoDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServerInterfaceInfoServiceImpl implements IAuthServerInterfaceInfoService{

    private static final Logger logger = LoggerFactory.getLogger(AuthServerInterfaceInfoServiceImpl.class);

    @Autowired
    IAuthServerInterfaceInfoDao authServerInterfaceInfoDao;

    /**
     * 创建第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param authServerInterfaceInfo
     */
    @Override
    public void create(AuthServerInterfaceInfo authServerInterfaceInfo) {
        if(null == authServerInterfaceInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        authServerInterfaceInfo.setCreateTime(now);
        authServerInterfaceInfo.setUpdateTime(now);
        authServerInterfaceInfoDao.insert(authServerInterfaceInfo);
    }

    /**
     * 修改第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param authServerInterfaceInfo
     */
    @Override
    public void modifyById(AuthServerInterfaceInfo authServerInterfaceInfo) {
        if(null == authServerInterfaceInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        authServerInterfaceInfo.setUpdateTime(new Date());
        authServerInterfaceInfoDao.updateById(authServerInterfaceInfo);
    }

    /**
     * 根据主键删除第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        AuthServerInterfaceInfo record = new AuthServerInterfaceInfo();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = authServerInterfaceInfoDao.updateById(record);
        if(count < 1){
            logger.error("AuthServerInterfaceInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public AuthServerInterfaceInfo getAuthServerInterfaceInfoById(Long id) {
        AuthServerInterfaceInfo result = null;
        if(null == id){
            return result;
        }
        result = authServerInterfaceInfoDao.getById(id);
        return result;
    }

}