package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.AuthServerInterfaceInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAuthServerInterfaceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authServerInterfaceInfo")
public class AuthServerInterfaceInfoAction{

    private static final Logger logger = LoggerFactory.getLogger(AuthServerInterfaceInfoAction.class);

    @Autowired
    private IAuthServerInterfaceInfoService authServerInterfaceInfoService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody AuthServerInterfaceInfo paramBean) {
        // TODO 参数校验
        authServerInterfaceInfoService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody AuthServerInterfaceInfo paramBean) {
        // TODO 参数校验
        authServerInterfaceInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        authServerInterfaceInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getAuthServerInterfaceInfoById", method = RequestMethod.POST)
    public ResponseResult<AuthServerInterfaceInfo> getAuthServerInterfaceInfoById(@RequestParam Long id) {
        AuthServerInterfaceInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = authServerInterfaceInfoService.getAuthServerInterfaceInfoById(id);
        return ResponseResult.success(result);
    }

}