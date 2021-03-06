package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.*;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.persistence.dao.*;
import com.glitter.spring.boot.service.IOauthClientRMService;
import com.glitter.spring.boot.service.IOauthCodeService;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthDeveloperRMService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OauthCodeServiceImpl implements IOauthCodeService {

    private static final Logger logger = LoggerFactory.getLogger(OauthCodeServiceImpl.class);

    @Autowired
    IOauthCodeDao oauthCodeDao;

    @Autowired
    IUserInfoDao userInfoDao;

    @Autowired
    IOauthClientInfoDao oauthClientInfoDao;

    @Autowired
    IOauthInterfaceEnumDao oauthInterfaceEnumDao;

    @Autowired
    IOauthScopeEnumDao oauthScopeEnumDao;

    @Autowired
    IOauthDeveloperRMDao oauthDeveloperRMDao;

    @Autowired
    IOauthDeveloperRMService oauthDeveloperRMService;

    @Autowired
    IOauthClientRMService oauthClientRMService;

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
        List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumDao.findAllList();
        Map<String,OauthScopeEnum> oauthScopeEnumsMap = oauthScopeEnums.stream().collect(Collectors.toMap((key -> key.getScopeName()), (value -> value)));
        List<String> scopes = Arrays.asList(oauthCode.getScope().split(","));
        for (int i = 0; i < scopes.size(); i++) {
            OauthScopeEnum oauthScopeEnum = oauthScopeEnumsMap.get(scopes.get(i));
            if (null == oauthScopeEnum) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,scope非法");
            }
            if (!GlitterConstants.OAUTH_GRANT_TYPE_AUTHORIZATION_CODE.equals(oauthScopeEnum.getGrantType())) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常,scope非法");
            }
        }

        // 2.获取用户在该客户端的openId
        String openId;
        OauthClientRM oauthClientRMDb = oauthClientRMService.getByClientIdAndUserId(oauthCode.getClientId(), oauthCode.getUserId());
        if(null == oauthClientRMDb){
            openId = oauthClientRMService.create(oauthCode.getClientId(), oauthCode.getUserId());
        } else {
            openId = oauthClientRMDb.getOpenId();
        }

        // 3.获取用户在该客户端所属开发账号的unionId
        String unionId;
        OauthDeveloperRM oauthDeveloperRM = oauthDeveloperRMService.getByDeveloperIdAndUserId(oauthClientInfo.getDeveloperId(), oauthCode.getUserId());
        if(null == oauthDeveloperRM){
            unionId = oauthDeveloperRMService.create(oauthClientInfo.getDeveloperId(), oauthCode.getUserId());
        } else {
            unionId = oauthDeveloperRM.getUnionId();
        }

        // 4.获取scope对应的interface接口
        List<OauthInterfaceEnum> oauthInterfaceEnums = oauthInterfaceEnumDao.getOauthInterfaceEnumByScopes(oauthCode.getScope().split(","));
        List<String> interfaces = (null == oauthInterfaceEnums || oauthInterfaceEnums.size() <= 0) ? null : oauthInterfaceEnums.stream().map(OauthInterfaceEnum::getScopeName).distinct().collect(Collectors.toList());
        String interfaceUri = (null == interfaces || interfaces.size() <= 0) ? "" : StringUtils.join(interfaces, ",");

        // 5.生成code码
        String code = UUID.randomUUID().toString();

        // 6.查询当前userId用户对clientId客户端是否存在有效的未过期的code码
        OauthCode oauthCodeDb = oauthCodeDao.getByUserIdAndClient(oauthCode.getUserId(), oauthCode.getClientId());
        if(null == oauthCodeDb){
            // 6.创建oauthCode
            Date now = new Date();
            oauthCode.setOpenId(openId);
            oauthCode.setUnionId(unionId);
            oauthCode.setCode(code);
            oauthCode.setInterfaceUri(interfaceUri);
            oauthCode.setCreateTime(now);
            oauthCode.setUpdateTime(now);
            oauthCode.setExpireIn(60L);
            oauthCode.setExpireTime(new Date(now.getTime() + oauthCode.getExpireIn() * 1000L));
            oauthCodeDao.insert(oauthCode);
        } else {
            // 7.更新oauthCode
            Date now = new Date();
            OauthCode record = new OauthCode();
            record.setId(oauthCodeDb.getId());
            record.setUserId(oauthCode.getUserId());
            record.setClientId(oauthCode.getClientId());
            record.setScope(oauthCode.getScope());
            record.setInterfaceUri(interfaceUri);
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