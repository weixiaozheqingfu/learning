package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.AccountBinding;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAccountBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accountBinding")
public class AccountBindingAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(AccountBindingAction.class);

    @Autowired
    private IAccountBindingService accountBindingService;

    public ResponseResult create(@RequestBody AccountBinding paramBean) {
        // TODO 参数校验
        accountBindingService.create(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult modifyById(@RequestBody AccountBinding paramBean) {
        // TODO 参数校验
        accountBindingService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        accountBindingService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getAccountBindingById", method = RequestMethod.POST)
    public ResponseResult<AccountBinding> getAccountBindingById(@RequestParam Long id) {
        AccountBinding result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = accountBindingService.getAccountBindingById(id);
        return ResponseResult.success(result);
    }

}