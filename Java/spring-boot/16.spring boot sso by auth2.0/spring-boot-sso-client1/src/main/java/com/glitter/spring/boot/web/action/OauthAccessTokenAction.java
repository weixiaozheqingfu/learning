package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthAccessToken")
public class OauthAccessTokenAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenAction.class);

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthAccessToken paramBean) {
        // TODO 参数校验
        oauthAccessTokenService.create(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody OauthAccessToken paramBean) {
        // TODO 参数校验
        oauthAccessTokenService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthAccessTokenService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthAccessTokenById", method = RequestMethod.POST)
    public ResponseResult<OauthAccessToken> getOauthAccessTokenById(@RequestParam Long id) {
        OauthAccessToken result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthAccessTokenService.getOauthAccessTokenById(id);
        return ResponseResult.success(result);
    }

}