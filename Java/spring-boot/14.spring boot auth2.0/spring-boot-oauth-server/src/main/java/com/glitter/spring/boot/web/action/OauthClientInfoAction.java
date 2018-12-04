package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthClientInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthClientInfo")
public class OauthClientInfoAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthClientInfoAction.class);

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthClientInfo paramBean) {
        // TODO 参数校验
        oauthClientInfoService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody OauthClientInfo paramBean) {
        // TODO 参数校验
        oauthClientInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthClientInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthClientInfoById", method = RequestMethod.POST)
    public ResponseResult<OauthClientInfo> getOauthClientInfoById(@RequestParam Long id) {
        OauthClientInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthClientInfoService.getOauthClientInfoById(id);
        return ResponseResult.success(result);
    }

}