package com.glitter.spring.boot.web.action.oauth;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.IUserInfoService;
import com.glitter.spring.boot.web.action.BaseAction;
import com.glitter.spring.boot.web.param.oauth.UserInfoResParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/oauth2/resource")
public class OauthResourceAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthResourceAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

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
     * 上传客户端局部会话的jsessionid。
     * 调用该方法需要传accessToken,因为accessToken不仅是用户授权凭证,可以访问用户授权的接口信息。
     * 在sso需求中,accessToken更是局部会话凭证,可以理解为用户授权客户端accessToken在这一次sso登录过程中访问用户信息。
     * accessToken能传过来，本身说明调用者是合法的也是经过用户授权的，同时accessToken也代表了其对应的是服务器端哪个jsessionid_sso会话。
     * 所以调用该接口虽然不是访问用户资源信息，你可以理解为跟oauth没有关系了，这里要传accessToken完全是sso功能的需要，并且由于前期的oauth铺垫，这里传accessToken也保证了客户端是合法的，用户是授权的前提。
     * 这就很巧妙了。
     *
     * @param jsessionid
     * @return
     */
    @RequestMapping(value = "uploadJsessionid", method = RequestMethod.POST)
    public ResponseResult uploadJsessionid(@RequestParam Long jsessionid) {
        // oauthAccessTokenService.updateJsessionIdClientByAccessToken(String accessToken, String jsessionIdClient);
        // TODO
        return ResponseResult.success(true);
    }

    /**
     * 客户端通过调用该方法,保持会话,即对服务器端会话进行续期。
     *
     * 同uploadJsessionid接口，调用该接口方法需要传accessToken，理由同上。
     * accessToken就是会话的凭证，accessToken对应了sso认证中心的jsessionid。
     *
     * @param jsessionid
     * @return
     */
    @RequestMapping(value = "keepAlive", method = RequestMethod.POST)
    public ResponseResult keepAlive(@RequestParam Long jsessionid) {
        // TODO
        return ResponseResult.success(true);
    }

    /**
     * 同uploadJsessionid接口，调用该接口方法需要传accessToken，理由同上。
     *
     * 另外该方法中回调客户端方法，是需要做rsa非对称加密的私钥签名的，这样客户端应用验证签名，知道调用方是sso认证中心，是合法的。
     * 当然认证方式有很多，首推rsa方式。这里只是为了演示流程，简单专一起见，将此签名验签过程省略。但生产环境真正做此功能，务必要加上。
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ResponseResult logout() {
        String jsessionid = AccessTokenInnerContext.get().getJsessionid();
        List<OauthAccessToken> oauthAccessTokens =  oauthAccessTokenService.getOauthAccessTokensByJsessionid(jsessionid);
        for (int i = 0; i < oauthAccessTokens.size(); i++) {
            OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(oauthAccessTokens.get(i).getClientId());

        }
        // TODO
        return ResponseResult.success(true);
    }


}