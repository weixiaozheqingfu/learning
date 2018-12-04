package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthClientScenarioConfig;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthClientScenarioConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauthClientScenarioConfig")
public class OauthClientScenarioConfigAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthClientScenarioConfigAction.class);

    @Autowired
    private IOauthClientScenarioConfigService oauthClientScenarioConfigService;

    public ResponseResult create(@RequestBody OauthClientScenarioConfig paramBean) {
        // TODO 参数校验
        oauthClientScenarioConfigService.create(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult modifyById(@RequestBody OauthClientScenarioConfig paramBean) {
        // TODO 参数校验
        oauthClientScenarioConfigService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        oauthClientScenarioConfigService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getOauthClientScenarioConfigById", method = RequestMethod.POST)
    public ResponseResult<OauthClientScenarioConfig> getOauthClientScenarioConfigById(@RequestParam Long id) {
        OauthClientScenarioConfig result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = oauthClientScenarioConfigService.getOauthClientScenarioConfigById(id);
        return ResponseResult.success(result);
    }

}