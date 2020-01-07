package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.AuthTokenInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAuthTokenInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authTokenInfo")
public class AuthTokenInfoAction{

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenInfoAction.class);

    @Autowired
    private IAuthTokenInfoService authTokenInfoService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody AuthTokenInfo paramBean) {
        // TODO 参数校验
        authTokenInfoService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody AuthTokenInfo paramBean) {
        // TODO 参数校验
        authTokenInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        authTokenInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getAuthTokenInfoById", method = RequestMethod.POST)
    public ResponseResult<AuthTokenInfo> getAuthTokenInfoById(@RequestParam Long id) {
        AuthTokenInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = authTokenInfoService.getAuthTokenInfoById(id);
        return ResponseResult.success(result);
    }

}