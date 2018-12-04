package com.glitter.spring.boot.web.action;


import com.glitter.spring.boot.bean.OauthInterfaceEnum;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthInterfaceEnumService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthInterfaceEnum")
public class OauthInterfaceEnumAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthInterfaceEnumAction.class);

    @Autowired
    private IOauthInterfaceEnumService oauthInterfaceEnumService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthInterfaceEnum paramBean) {
        // TODO 参数校验
        oauthInterfaceEnumService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody OauthInterfaceEnum paramBean) {
        // TODO 参数校验
        oauthInterfaceEnumService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthInterfaceEnumService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthInterfaceEnumById", method = RequestMethod.POST)
    public ResponseResult<OauthInterfaceEnum> getOauthInterfaceEnumById(@RequestParam Long id) {
        OauthInterfaceEnum result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthInterfaceEnumService.getOauthInterfaceEnumById(id);
        return ResponseResult.success(result);
    }

}