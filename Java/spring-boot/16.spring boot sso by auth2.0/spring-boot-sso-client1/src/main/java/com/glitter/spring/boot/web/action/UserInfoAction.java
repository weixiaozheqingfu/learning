package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);
	
	@RequestMapping(value = "getLoginUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfoById() {
        UserInfo result = (UserInfo) sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
        return ResponseResult.success(result);
    }

}