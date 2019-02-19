package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.AccountBinding;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IAccountBindingDao;
import com.glitter.spring.boot.service.IAccountBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountBindingServiceImpl implements IAccountBindingService {

    private static final Logger logger = LoggerFactory.getLogger(AccountBindingServiceImpl.class);

    @Autowired
    IAccountBindingDao accountBindingDao;

    /**
     * 创建第三方账户绑定表; InnoDB free: 488448 kB
     * @param accountBinding
     */
    @Override
    public void create(AccountBinding accountBinding) {
        if(null == accountBinding){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS,"输入参数为空");
        }
        // TODO 参数校验
        Date now = new Date();
        accountBinding.setCreateTime(now);
        accountBinding.setUpdateTime(now);
        accountBindingDao.insert(accountBinding);
    }

    /**
     * 修改第三方账户绑定表; InnoDB free: 488448 kB
     * @param accountBinding
     */
    @Override
    public void modifyById(AccountBinding accountBinding) {
        if(null == accountBinding){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        // TODO 参数校验
        accountBinding.setUpdateTime(new Date());
        accountBindingDao.updateById(accountBinding);
    }

    /**
     * 根据主键删除第三方账户绑定表; InnoDB free: 488448 kB
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if(null == id){
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数为空");
        }
        AccountBinding record = new AccountBinding();
        record.setId(id);
        record.setUpdateTime(new Date());
        int count = accountBindingDao.updateById(record);
        if(count < 1){
            logger.error("AccountBindingServiceImpl.deleteById方法执行失败,输入参数:{}",id);
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "操作失败");
        }
    }

    /**
     * 根据主键获取第三方账户绑定表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    @Override
    public AccountBinding getAccountBindingById(Long id) {
        AccountBinding result = null;
        if(null == id){
            return result;
        }
        result = accountBindingDao.getAccountBindingById(id);
        return result;
    }

}