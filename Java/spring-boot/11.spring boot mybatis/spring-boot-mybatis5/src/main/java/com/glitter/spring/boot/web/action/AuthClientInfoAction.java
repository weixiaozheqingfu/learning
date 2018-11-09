package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.AuthClientInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAuthClientInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authClientInfo")
public class AuthClientInfoAction{

    private static final Logger logger = LoggerFactory.getLogger(AuthClientInfoAction.class);

    @Autowired
    private IAuthClientInfoService authClientInfoService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody AuthClientInfo paramBean) {
        // TODO 参数校验
        authClientInfoService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody AuthClientInfo paramBean) {
        // TODO 参数校验
        authClientInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        authClientInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getAuthClientInfoById", method = RequestMethod.POST)
    public ResponseResult<AuthClientInfo> getAuthClientInfoById(@RequestParam Long id) {
        AuthClientInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = authClientInfoService.getAuthClientInfoById(id);
        return ResponseResult.success(result);
    }

}