package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthServerEnumService;
import com.glitter.spring.boot.bean.OauthServerEnum;
import com.glitter.spring.boot.persistence.dao.IOauthServerEnumDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthServerEnumServiceImpl implements IOauthServerEnumService{

    private static final Logger logger = LoggerFactory.getLogger(OauthServerEnumServiceImpl.class);

    @Autowired
    IOauthServerEnumDao oauthServerEnumDao;

    /**
     * 创建第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param oauthServerEnum
     */
    @Override
    public void create(OauthServerEnum oauthServerEnum) {
        if(null == oauthServerEnum){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthServerEnum.setCreateTime(now);
        oauthServerEnum.setUpdateTime(now);
        oauthServerEnumDao.insert(oauthServerEnum);
    }

    /**
     * 修改第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param oauthServerEnum
     */
    @Override
    public void modifyById(OauthServerEnum oauthServerEnum) {
        if(null == oauthServerEnum){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthServerEnum.setUpdateTime(new Date());
        oauthServerEnumDao.updateById(oauthServerEnum);
    }

    /**
     * 根据主键删除第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthServerEnum record = new OauthServerEnum();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthServerEnumDao.updateById(record);
        if(count < 1){
            logger.error("OauthServerEnumServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthServerEnum getOauthServerEnumById(Long id) {
        OauthServerEnum result = null;
        if(null == id){
            return result;
        }
        result = oauthServerEnumDao.getOauthServerEnumById(id);
        return result;
    }

}