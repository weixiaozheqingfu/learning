package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthClientRM;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthClientRMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthClientRM")
public class OauthClientRMAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthClientRMAction.class);

    @Autowired
    private IOauthClientRMService oauthClientRMService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthClientRM paramBean) {
        // TODO 参数校验
        oauthClientRMService.create(paramBean.getClientId(), paramBean.getUserId());
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody OauthClientRM paramBean) {
        // TODO 参数校验
        oauthClientRMService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthClientRMService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthClientRMById", method = RequestMethod.POST)
    public ResponseResult<OauthClientRM> getOauthClientRMById(@RequestParam Long id) {
        OauthClientRM result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthClientRMService.getById(id);
        return ResponseResult.success(result);
    }

}