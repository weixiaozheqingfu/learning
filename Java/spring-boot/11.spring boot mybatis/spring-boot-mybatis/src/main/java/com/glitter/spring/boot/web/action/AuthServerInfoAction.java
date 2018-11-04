package com.glitter.spring.boot.web.action;


import com.glitter.spring.boot.bean.AuthServerInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IAuthServerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authServerInfo")
public class AuthServerInfoAction{

    private static final Logger logger = LoggerFactory.getLogger(AuthServerInfoAction.class);

    @Autowired
    private IAuthServerInfoService authServerInfoService;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody AuthServerInfo paramBean) {
        // TODO 参数校验
        authServerInfoService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody AuthServerInfo paramBean) {
        // TODO 参数校验
        authServerInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        authServerInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getAuthServerInfoById", method = RequestMethod.POST)
    public ResponseResult<AuthServerInfo> getAuthServerInfoById(@RequestParam Long id) {
        AuthServerInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = authServerInfoService.getAuthServerInfoById(id);
        return ResponseResult.success(result);
    }

}