package com.glitter.spring.boot.service.impl;


import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.plugin.page.PageHelper;
import com.glitter.spring.boot.plugin.page.PageInfo;
import com.glitter.spring.boot.service.IUserInfoService;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.persistence.dao.IUserInfoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserInfoServiceImpl implements IUserInfoService{

    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    IUserInfoDao userInfoDao;

    /**
     * 创建用户表; InnoDB free: 488448 kB
     * @param userInfo
     */
    @Override
    public void create(UserInfo userInfo) {
        if(null == userInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        userInfo.setCreateTime(now);
        userInfo.setUpdateTime(now);
        userInfo.setDeleteFlag(false);
        userInfoDao.insert(userInfo);
    }

    /**
     * 修改用户表; InnoDB free: 488448 kB
     * @param userInfo
     */
    @Override
    public void modifyById(UserInfo userInfo) {
        if(null == userInfo){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        userInfo.setUpdateTime(new Date());
        userInfoDao.updateById(userInfo);
    }

    /**
     * 根据主键删除用户表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        UserInfo record = new UserInfo();
        record.setId(id);
        record.setDeleteFlag(true);
        record.setUpdateTime(new Date());
        int count = userInfoDao.updateById(record);
        if(count < 1){
            logger.error("UserInfoServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }


//    @Override
//    public PageInfo<UserInfo> getUserInfosPage(Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<UserInfo> userInfos = userInfoDao.findAllList();
//        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfos);
//        return pageInfo;
//    }

    @Override
    public PageInfo<UserInfo> getUserInfosPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfo> userInfos = userInfoDao.findAllList();
        PageInfo<UserInfo> pageInfo = new PageInfo(userInfos);

        // TODO 测试一个方法中连续分页的情况,或者说再执行其他查询sql会发生什么情况,再试一下github的分页插件是什么情况，如果后面的sql不需要分页，则需要在sql执行前调用PageHelper.clearPage();
        // 建议PageInfo<UserInfo> pageInfo = new PageInfo(userInfos);中将threadLocal清空。
        return pageInfo;
    }

    /**
     * 根据主键获取用户表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public UserInfo getUserInfoById(Long id) {
        UserInfo result = null;
        if(null == id){
            return result;
        }
        result = userInfoDao.getById(id);
        return result;
    }

}