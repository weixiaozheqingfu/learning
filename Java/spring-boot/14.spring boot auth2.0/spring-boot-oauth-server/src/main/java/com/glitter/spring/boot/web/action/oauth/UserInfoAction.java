package com.glitter.spring.boot.web.action.oauth;


import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.service.IUserInfoService;
import com.glitter.spring.boot.web.action.BaseAction;
import com.glitter.spring.boot.web.param.oauth.UserInfoResParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class UserInfoAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @RequestMapping(value = "userinfo", method = RequestMethod.GET)
    public ResponseResult<UserInfoResParam> getUserInfo() {
        UserInfo userInfo = userInfoService.getUserInfoById(AccessTokenInnerContext.get().getUserId());
        if(null == userInfo){
            return null;
        }

        UserInfoResParam result = new UserInfoResParam();
        result.setOpenId(AccessTokenInnerContext.get().getOpenId());
        result.setUnionId(AccessTokenInnerContext.get().getUnionId());
        result.setNickName(userInfo.getNickName());
        result.setAge(userInfo.getAge());
        result.setSex(userInfo.getSex());
        return ResponseResult.success(result);

    }

}