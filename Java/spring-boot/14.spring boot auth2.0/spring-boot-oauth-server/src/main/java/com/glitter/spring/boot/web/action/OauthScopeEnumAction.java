package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthScopeEnum;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthScopeEnumService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthScopeEnum")
public class OauthScopeEnumAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthScopeEnumAction.class);

    @Autowired
    private IOauthScopeEnumService oauthScopeEnumService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthScopeEnum paramBean) {
        // TODO 参数校验
        oauthScopeEnumService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody OauthScopeEnum paramBean) {
        // TODO 参数校验
        oauthScopeEnumService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthScopeEnumService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthScopeEnumById", method = RequestMethod.POST)
    public ResponseResult<OauthScopeEnum> getOauthScopeEnumById(@RequestParam Long id) {
        OauthScopeEnum result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthScopeEnumService.getOauthScopeEnumById(id);
        return ResponseResult.success(result);
    }

}