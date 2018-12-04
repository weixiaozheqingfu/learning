package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthClientBaseConfig;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthClientBaseConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthClientBaseConfig")
public class OauthClientBaseConfigAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthClientBaseConfigAction.class);

    @Autowired
    private IOauthClientBaseConfigService oauthClientBaseConfigService;

    public ResponseResult create(@RequestBody OauthClientBaseConfig paramBean) {
        // TODO 参数校验
        oauthClientBaseConfigService.create(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult modifyById(@RequestBody OauthClientBaseConfig paramBean) {
        // TODO 参数校验
        oauthClientBaseConfigService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthClientBaseConfigService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthClientBaseConfigById", method = RequestMethod.POST)
    public ResponseResult<OauthClientBaseConfig> getOauthClientBaseConfigById(@RequestParam Long id) {
        OauthClientBaseConfig result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthClientBaseConfigService.getOauthClientBaseConfigById(id);
        return ResponseResult.success(result);
    }

}