package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    public ResponseResult create(@RequestBody UserInfo paramBean) {
        // TODO 参数校验
        userInfoService.create(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult modifyById(@RequestBody UserInfo paramBean) {
        // TODO 参数校验
        userInfoService.modifyById(paramBean);
        return ResponseResult.success(null);
    }

    public ResponseResult deleteById(@RequestParam Long id) {
        if(null == id) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "输入参数异常，id值为空!");
        }
        userInfoService.deleteById(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "getUserInfoById", method = RequestMethod.POST)
    public ResponseResult<UserInfo> getUserInfoById(@RequestParam Long id) {
        UserInfo result = null;
        if(null == id) {
            return ResponseResult.success(result);
        }
        result = userInfoService.getUserInfoById(id);
        return ResponseResult.success(result);
    }
	
	@RequestMapping(value = "getLoginUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfoById() {
        UserInfo result = (UserInfo) sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
        return ResponseResult.success(result);
    }

}