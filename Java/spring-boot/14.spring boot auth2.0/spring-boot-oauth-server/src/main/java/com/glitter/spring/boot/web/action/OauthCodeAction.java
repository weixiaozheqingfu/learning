package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthCode;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthCodeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthCode")
public class OauthCodeAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthCodeAction.class);

    @Autowired
    private IOauthCodeService oauthCodeService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody OauthCode paramBean) {
        // TODO 参数校验
        oauthCodeService.generateCode(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthCodeService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthCodeById", method = RequestMethod.POST)
    public ResponseResult<OauthCode> getOauthCodeById(@RequestParam Long id) {
        OauthCode result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthCodeService.getOauthCodeById(id);
        return ResponseResult.success(result);
    }

}