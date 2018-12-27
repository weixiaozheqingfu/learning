package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OauthAccessTokenServiceImpl implements IOauthAccessTokenService{

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenServiceImpl.class);

    @Autowired
    IOauthAccessTokenDao oauthAccessTokenDao;

    /**
     * 验证accessToken,如果验证通过,则返回该accessToken对应的授权人openid,只有返回openid,本次的accessToken才是合法有效的
     * <p>
     * 只要accessToken是合法的,就能说明当前的请求是一个合法客户端的请求,并且本次具体请求的interfaceUri地址也在accessToken的授权范围内,一切都在红线内,
     * 只是客户端请求的是服务器资源方,资源方是不知道当前的accessToken的对应的授权人信息的,需要将此信息返回给服务器资源方,告诉服务器资源方本次请求接口逻辑中只能处理该授权人相关的数据,数据不能越界.
     * <p>
     * (有的设计为了方便,在返回accessToken的时候同时返回了openid,然后调用资源方接口的时候,同时传入accessToken和openid,这种情况,资源法调用完validateAccessToken接口后
     * 需要进一步根据validateAccessToken接口返回的openid与客户端传入的openid对比是否一致,一致才能访问接口,否则失败.
     * 这种服务器资源方的接口设计也可以,他让客户端自己以参数的形式指明本次调用的接口要访问的数据范围即openid,然后服务器资源方接收到参数明白了当前客户端要访问这openid的数据,就看他的
     * accessToken验证返回结果是不是这个openid,是的话说明没问题继续执行服务器接口逻辑并返回结果,反之,则客户端故意请求授权外的openid,企图蒙混过关,则服务器资源方进行异常抛出返回即可。
     * )
     *
     * @param accessToken
     * @param interfaceUri
     * @return
     */
    @Override
    public Long validateAccessToken(String accessToken, String interfaceUri) {
        // 1.验证accessToken是否存在

        // 2.验证accessToken是否在有效期内

        // 3.至此说明当前的发起请求的客户端可以访问openid的scope范围的接口数据,下面就看客户端本次发起的interfaceUri接口是否在scope授权范围内

        // 4.验证当前请求接口interfaceUri是否在accessToken授权范围内

        // 5.至此说明当前客户端发起了一个非常合法的请求,就是要访问授权人openid的interfaceUri接口

        // 6.返回授权人openid,让服务器资源方知道当前接口的数据权限范围是局限在openid中的。

        return null;
    }


    /**
     * 根据openid获取系统中对应的userId
     *
     * @param openid
     * @return
     */
    @Override
    public Long getUserIdByOpenid(String openid) {
        return null;
    }

    /**
     * 根据主键删除accessToken
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        OauthAccessToken record = new OauthAccessToken();
        record.setId(id);
        record.setDeleteFlag(true);
        record.setUpdateTime(new Date());
        int count = oauthAccessTokenDao.updateById(record);
        if(count < 1){
            logger.error("OauthAccessTokenServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }



}