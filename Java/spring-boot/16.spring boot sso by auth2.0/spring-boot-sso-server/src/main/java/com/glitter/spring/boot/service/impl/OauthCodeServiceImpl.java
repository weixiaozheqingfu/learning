package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.OauthCode;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthClientInfoDao;
import com.glitter.spring.boot.persistence.dao.IOauthCodeDao;
import com.glitter.spring.boot.persistence.dao.IUserInfoDao;
import com.glitter.spring.boot.service.IOauthCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OauthCodeServiceImpl implements IOauthCodeService {

    private static final Logger logger = LoggerFactory.getLogger(OauthCodeServiceImpl.class);

    @Autowired
    IOauthCodeDao oauthCodeDao;

    @Autowired
    IUserInfoDao userInfoDao;

    @Autowired
    IOauthClientInfoDao oauthClientInfoDao;

    /**
     * 授权码模式 创建预授权码
     *
     * @param oauthCode
     */
    @Override
    public String generateCode(OauthCode oauthCode) {
        // 1.校验参数合法性
        if (null == oauthCode) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        if (null == oauthCode.getUserId()) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,userId为空");
        }
        if (StringUtils.isBlank(oauthCode.getClientId())) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,clientId为空");
        }
        if (StringUtils.isBlank(oauthCode.getScope())) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,scope为空");
        }
        if (null == userInfoDao.getUserInfoById(oauthCode.getUserId())) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,userId非法");
        }
        OauthClientInfo oauthClientInfo = oauthClientInfoDao.getOauthClientInfoByClientId(oauthCode.getClientId());
        if (null == oauthClientInfo) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,userId非法");
        }
        if (!oauthCode.getScope().equals("get_user_open_info")) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,scope非法");
        }

        // 1.生成code码
        String code = UUID.randomUUID().toString();

        // 2.删除过期code码,这一步可以做,因为过期后,在库中呆着也无妨,只是看着碍眼
        oauthCodeDao.deleteByUserIdAndClient(oauthCode.getJsessionId(), oauthCode.getUserId(), oauthCode.getClientId());

        // 3.查询当前jsessionId下,userId用户对clientId客户端是否存在有效的未过期的code码
        OauthCode oauthCodeDb = oauthCodeDao.getByUserIdAndClient(oauthCode.getJsessionId(), oauthCode.getUserId(), oauthCode.getClientId());
        if(null == oauthCodeDb){
            // 4.创建oauthCode
            Date now = new Date();
            oauthCode.setCode(code);
            oauthCode.setCreateTime(now);
            oauthCode.setUpdateTime(now);
            oauthCode.setExpireIn(60L);
            oauthCode.setExpireTime(new Date(now.getTime() + oauthCode.getExpireIn() * 1000L));
            oauthCodeDao.insert(oauthCode);
        } else {
            // 4.更新oauthCode
            Date now = new Date();
            OauthCode record = new OauthCode();
            record.setId(oauthCodeDb.getId());
            record.setUserId(oauthCode.getUserId());
            record.setClientId(oauthCode.getClientId());
            record.setScope(oauthCode.getScope());
            record.setCode(code);
            record.setUpdateTime(now);
            record.setExpireIn(60L);
            record.setExpireTime(new Date(now.getTime() + record.getExpireIn() * 1000L));
            oauthCodeDao.updateById(record);
        }
        return code;
    }

    /**
     * 根据主键删除预授权码
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if (null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        int count = oauthCodeDao.deleteById(id);
        if (count < 1) {
            logger.error("OauthCodeServiceImpl.deleteById方法执行失败,输入参数:{}", id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    @Override
    public void deleteByCode(String code) {
        if (null == code) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        int count = oauthCodeDao.deleteByCode(code);
        if (count < 1) {
            logger.error("OauthCodeServiceImpl.deleteByCode方法执行失败,输入参数:{}", code);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取预授权码
     *
     * @param id
     * @return
     */
    @Override
    public OauthCode getOauthCodeById(Long id) {
        OauthCode result = null;
        if (null == id) {
            return result;
        }
        result = oauthCodeDao.getById(id);
        return result;
    }

    @Override
    public OauthCode getOauthCodeByCode(String code) {
        OauthCode result = null;
        if (StringUtils.isBlank(code)) {
            return result;
        }
        OauthCode record = new OauthCode();
        record.setCode(code);
        result = oauthCodeDao.get(record);
        return result;
    }

}