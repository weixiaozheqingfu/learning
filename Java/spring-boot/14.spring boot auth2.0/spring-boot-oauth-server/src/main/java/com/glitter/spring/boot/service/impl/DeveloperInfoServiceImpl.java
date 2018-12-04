package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IDeveloperInfoService;
import com.glitter.spring.boot.bean.DeveloperInfo;
import com.glitter.spring.boot.persistence.dao.IDeveloperInfoDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeveloperInfoServiceImpl implements IDeveloperInfoService{

    private static final Logger logger = LoggerFactory.getLogger(DeveloperInfoServiceImpl.class);

    @Autowired
    IDeveloperInfoDao developerInfoDao;

    /**
     * 创建开发者账号信息表; InnoDB free: 488448 kB
     * @param developerInfo
     */
    @Override
    public void create(DeveloperInfo developerInfo) {
        if(null == developerInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        developerInfo.setCreateTime(now);
        developerInfo.setUpdateTime(now);
        developerInfoDao.insert(developerInfo);
    }

    /**
     * 修改开发者账号信息表; InnoDB free: 488448 kB
     * @param developerInfo
     */
    @Override
    public void modifyById(DeveloperInfo developerInfo) {
        if(null == developerInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        developerInfo.setUpdateTime(new Date());
        developerInfoDao.updateById(developerInfo);
    }

    /**
     * 根据主键删除开发者账号信息表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        DeveloperInfo record = new DeveloperInfo();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = developerInfoDao.updateById(record);
        if(count < 1){
            logger.error("DeveloperInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取开发者账号信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public DeveloperInfo getDeveloperInfoById(Long id) {
        DeveloperInfo result = null;
        if(null == id){
            return result;
        }
        result = developerInfoDao.getDeveloperInfoById(id);
        return result;
    }

}