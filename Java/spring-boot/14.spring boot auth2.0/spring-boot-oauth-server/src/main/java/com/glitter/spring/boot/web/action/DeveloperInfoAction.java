package com.glitter.spring.boot.web.action;


import com.glitter.spring.boot.bean.DeveloperInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IDeveloperInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/developerInfo")
public class DeveloperInfoAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(DeveloperInfoAction.class);

    @Autowired
    private IDeveloperInfoService developerInfoService;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseResult create(@RequestBody DeveloperInfo paramBean) {
        // TODO 参数校验
        developerInfoService.create(paramBean);
        return ResponseResult.success(null);
    }


    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public ResponseResult modifyById(@RequestBody DeveloperInfo paramBean) {
        // TODO 参数校验
        developerInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "deleteById", method = RequestMethod.POST)
    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        developerInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getDeveloperInfoById", method = RequestMethod.POST)
    public ResponseResult<DeveloperInfo> getDeveloperInfoById(@RequestParam Long id) {
        DeveloperInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = developerInfoService.getDeveloperInfoById(id);
        return ResponseResult.success(result);
    }

}