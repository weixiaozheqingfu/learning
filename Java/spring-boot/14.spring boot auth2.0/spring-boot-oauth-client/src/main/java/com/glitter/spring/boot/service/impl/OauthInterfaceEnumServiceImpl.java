package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthInterfaceEnumService;
import com.glitter.spring.boot.bean.OauthInterfaceEnum;
import com.glitter.spring.boot.persistence.dao.IOauthInterfaceEnumDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthInterfaceEnumServiceImpl implements IOauthInterfaceEnumService{

    private static final Logger logger = LoggerFactory.getLogger(OauthInterfaceEnumServiceImpl.class);

    @Autowired
    IOauthInterfaceEnumDao oauthInterfaceEnumDao;

    /**
     * 创建授权接口枚举表; InnoDB free: 488448 kB
     * @param oauthInterfaceEnum
     */
    @Override
    public void create(OauthInterfaceEnum oauthInterfaceEnum) {
        if(null == oauthInterfaceEnum){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthInterfaceEnum.setCreateTime(now);
        oauthInterfaceEnum.setUpdateTime(now);
        oauthInterfaceEnumDao.insert(oauthInterfaceEnum);
    }

    /**
     * 修改授权接口枚举表; InnoDB free: 488448 kB
     * @param oauthInterfaceEnum
     */
    @Override
    public void modifyById(OauthInterfaceEnum oauthInterfaceEnum) {
        if(null == oauthInterfaceEnum){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthInterfaceEnum.setUpdateTime(new Date());
        oauthInterfaceEnumDao.updateById(oauthInterfaceEnum);
    }

    /**
     * 根据主键删除授权接口枚举表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthInterfaceEnum record = new OauthInterfaceEnum();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthInterfaceEnumDao.updateById(record);
        if(count < 1){
            logger.error("OauthInterfaceEnumServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取授权接口枚举表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthInterfaceEnum getOauthInterfaceEnumById(Long id) {
        OauthInterfaceEnum result = null;
        if(null == id){
            return result;
        }
        result = oauthInterfaceEnumDao.getOauthInterfaceEnumById(id);
        return result;
    }

}