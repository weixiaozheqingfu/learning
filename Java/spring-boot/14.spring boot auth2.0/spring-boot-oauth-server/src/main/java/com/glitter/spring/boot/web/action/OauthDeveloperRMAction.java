package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthDeveloperRM;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthDeveloperRMService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthDeveloperRM")
public class OauthDeveloperRMAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthDeveloperRMAction.class);

    @Autowired
    private IOauthDeveloperRMService oauthDeveloperRMService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthDeveloperRM paramBean) {
        // TODO 参数校验
        oauthDeveloperRMService.create(paramBean.getDeveloperId(), paramBean.getUserId());
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody OauthDeveloperRM paramBean) {
        // TODO 参数校验
        oauthDeveloperRMService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");        }
        oauthDeveloperRMService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthDeveloperRMById", method = RequestMethod.POST)
    public ResponseResult<OauthDeveloperRM> getOauthDeveloperRMById(@RequestParam Long id) {
        OauthDeveloperRM result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthDeveloperRMService.getOauthDeveloperRMById(id);
        return ResponseResult.success(result);
    }

}