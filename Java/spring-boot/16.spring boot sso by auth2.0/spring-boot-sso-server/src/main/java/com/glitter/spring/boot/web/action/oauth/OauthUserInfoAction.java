package com.glitter.spring.boot.web.action.oauth;


import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
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
@RequestMapping("/oauth2/resource")
public class OauthUserInfoAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthUserInfoAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    @RequestMapping(value = "userinfo", method = RequestMethod.GET)
    public ResponseResult<UserInfoResParam> getUserInfo() {
        UserInfo userInfo = userInfoService.getUserInfoById(AccessTokenInnerContext.get().getUserId());
        if(null == userInfo){
            return null;
        }
        UserInfoResParam result = new UserInfoResParam();
        result.setUserId(AccessTokenInnerContext.get().getUserId());
        result.setNickName(userInfo.getNickName());
        result.setAge(userInfo.getAge());
        result.setSex(userInfo.getSex());
        return ResponseResult.success(result);
    }

    /**
     * 客户端应用调用该方法采用重定向方式，该方法亦会重定向方式回调客户端。
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ResponseResult logout() {
        oauthAccessTokenService.validateAccessToken(AccessTokenInnerContext.get().getAccess_token());
        OauthAccessToken oauthAccessToken = oauthAccessTokenService.getOauthAccessToken(AccessTokenInnerContext.get().getAccess_token());
        if(null == oauthAccessToken){
            throw new BusinessException("50036", "accessToken失效");
        }
        String jsessionid = oauthAccessToken.getJsessionId();
        oauthAccessTokenService.getOauthAccessTokensByJsessionid(jsessionid);
        // TODO
        return ResponseResult.success(true);
    }
}