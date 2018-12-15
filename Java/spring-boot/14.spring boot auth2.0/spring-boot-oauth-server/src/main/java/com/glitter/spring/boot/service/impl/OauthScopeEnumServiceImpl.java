package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthScopeEnumService;
import com.glitter.spring.boot.bean.OauthScopeEnum;
import com.glitter.spring.boot.persistence.dao.IOauthScopeEnumDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OauthScopeEnumServiceImpl implements IOauthScopeEnumService{

    private static final Logger logger = LoggerFactory.getLogger(OauthScopeEnumServiceImpl.class);

    @Autowired
    IOauthScopeEnumDao oauthScopeEnumDao;

    /**
     * 创建授权作用域枚举表; InnoDB free: 488448 kB
     * @param oauthScopeEnum
     */
    @Override
    public void create(OauthScopeEnum oauthScopeEnum) {
        if(null == oauthScopeEnum){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        oauthScopeEnum.setCreateTime(now);
        oauthScopeEnum.setUpdateTime(now);
        oauthScopeEnumDao.insert(oauthScopeEnum);
    }

    /**
     * 修改授权作用域枚举表; InnoDB free: 488448 kB
     * @param oauthScopeEnum
     */
    @Override
    public void modifyById(OauthScopeEnum oauthScopeEnum) {
        if(null == oauthScopeEnum){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        oauthScopeEnum.setUpdateTime(new Date());
        oauthScopeEnumDao.updateById(oauthScopeEnum);
    }

    /**
     * 根据主键删除授权作用域枚举表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthScopeEnum record = new OauthScopeEnum();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = oauthScopeEnumDao.updateById(record);
        if(count < 1){
            logger.error("OauthScopeEnumServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取授权作用域枚举表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public OauthScopeEnum getById(Long id) {
        OauthScopeEnum result = null;
        if(null == id){
            return result;
        }
        result = oauthScopeEnumDao.getOauthScopeEnumById(id);
        return result;
    }

    @Override
    public List<OauthScopeEnum> getByScopeNames(List<String> scopeNames) {
        List<OauthScopeEnum> result = null;
        if(null == scopeNames || scopeNames.size() <=0){
            return result;
        }
        result = oauthScopeEnumDao.getOauthScopeEnumByScopeNames(scopeNames);
        return result;
    }

    @Override
    public List<OauthScopeEnum> getAll() {
        return oauthScopeEnumDao.findAllList();
    }
}