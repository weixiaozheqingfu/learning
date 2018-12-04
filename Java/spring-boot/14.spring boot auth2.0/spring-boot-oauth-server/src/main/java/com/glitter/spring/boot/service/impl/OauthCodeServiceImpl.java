package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthCodeService;
import com.glitter.spring.boot.bean.OauthCode;
import com.glitter.spring.boot.persistence.dao.IOauthCodeDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthCodeServiceImpl implements IOauthCodeService{

    private static final Logger logger = LoggerFactory.getLogger(OauthCodeServiceImpl.class);

    @Autowired
    IOauthCodeDao oauthCodeDao;

    /**
     * 创建预授权码表; InnoDB free: 488448 kB
     * @param oauthCode
     */
    @Override
    public void create(OauthCode oauthCode) {
        if(null == oauthCode){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthCode.setCreateTime(now);
        oauthCode.setUpdateTime(now);
        oauthCode.setDeleteFlag(false);
        oauthCodeDao.insert(oauthCode);
    }

    /**
     * 修改预授权码表; InnoDB free: 488448 kB
     * @param oauthCode
     */
    @Override
    public void modifyById(OauthCode oauthCode) {
        if(null == oauthCode){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthCode.setUpdateTime(new Date());
        oauthCodeDao.updateById(oauthCode);
    }

    /**
     * 根据主键删除预授权码表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthCode record = new OauthCode();
        record.setId(id);
        record.setDeleteFlag(true);
        record.setUpdateTime(new Date());
        int count = oauthCodeDao.updateById(record);
        if(count < 1){
            logger.error("OauthCodeServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取预授权码表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthCode getOauthCodeById(Long id) {
        OauthCode result = null;
        if(null == id){
            return result;
        }
        result = oauthCodeDao.getOauthCodeById(id);
        return result;
    }

}