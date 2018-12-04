package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthServerEnum;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthServerEnumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthServerEnum")
public class OauthServerEnumAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthServerEnumAction.class);

    @Autowired
    private IOauthServerEnumService oauthServerEnumService;

    public ResponseResult create(@RequestBody OauthServerEnum paramBean) {
        // TODO 参数校验
        oauthServerEnumService.create(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult modifyById(@RequestBody OauthServerEnum paramBean) {
        // TODO 参数校验
        oauthServerEnumService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthServerEnumService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthServerEnumById", method = RequestMethod.POST)
    public ResponseResult<OauthServerEnum> getOauthServerEnumById(@RequestParam Long id) {
        OauthServerEnum result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthServerEnumService.getOauthServerEnumById(id);
        return ResponseResult.success(result);
    }

}