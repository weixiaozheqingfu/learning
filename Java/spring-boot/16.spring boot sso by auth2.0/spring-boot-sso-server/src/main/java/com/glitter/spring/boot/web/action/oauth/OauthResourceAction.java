package com.glitter.spring.boot.web.action.oauth;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.context.AccessTokenInnerContext;
import com.glitter.spring.boot.persistence.remote.IClientRemote;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.ISessionHandler;
import com.glitter.spring.boot.service.IUserInfoService;
import com.glitter.spring.boot.web.action.BaseAction;
import com.glitter.spring.boot.web.param.oauth.UserInfoResParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sso/resource")
public class OauthResourceAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthResourceAction.class);

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;
    @Autowired
    private IOauthClientInfoService oauthClientInfoService;
    @Autowired
    private ISessionHandler sessionHandler;
    @Autowired
    private IClientRemote clientRemote;

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
     * 客户端通过调用该方法,保持会话,即对服务器端会话进行续期。
     *
     * 调用该方法需要传accessToken,因为accessToken不仅是用户授权凭证,可以访问用户授权的接口信息。
     * 在sso需求中,,accessToken更是局部会话凭证,accessToken能传过来，本身说明调用者是合法的也是经过用户授权的，
     * 同时accessToken也代表了其对应的是服务器端哪个jsessionid_sso会话。
     *
     * accessToken就是全局会话下针对每一个客户端会话的凭证和代表，accessToken对应了sso认证中心的jsessionid。
     * 所以调用该接口虽然不是访问用户资源信息，你可以理解为跟oauth没有关系了，这里要传accessToken完全是sso功能的需要，
     * 并且由于前期的oauth铺垫，这里传accessToken也保证了客户端是合法的，用户授权是前提。
     * 这就很巧妙了。
     *
     * @return
     */
    @RequestMapping(value = "keepAlive", method = RequestMethod.POST)
    public ResponseResult keepAlive() {
        // String jsessionid = AccessTokenInnerContext.get().getJsessionid();
        // 拦截器已完成续期,此处不需要再续期,该接口可以理解为标记接口,方法内部不做任何处理。
        // sessionHandler.renewal(jsessionid);
        return ResponseResult.success(true);
    }

    /**
     * 同uploadJsessionid接口，调用该接口方法需要传accessToken，理由同上。
     *
     * 另外该方法中回调客户端方法，是需要做rsa非对称加密的私钥签名的，这样客户端应用验证签名，知道调用方是sso认证中心，是合法的。
     * 当然认证方式有很多，首推rsa方式。这里只是为了演示流程，简单专一起见，将此签名验签过程省略。但生产环境真正做此功能，务必要加上。
     *
     * 客户端收到该方法的返回值成功后，需要重定向到其请求登录授权页。这样重新登录后会跳转到这个客户端的首页。
     * 即用户是通过哪个客户端退出的，就跳转到哪个客户端的登录接口，由这个客户端再次发起sso登录，这样登录完成后，还是在这个客户端首页。
     * 这样给用户的体验是完整的舒适的。
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ResponseResult logout() {
        String jsessionid = AccessTokenInnerContext.get().getJsessionid();
        List<OauthAccessToken> oauthAccessTokens =  oauthAccessTokenService.getOauthAccessTokensByJsessionid(jsessionid);
        // 注销所有客户端登录
        for (int i = 0; i < oauthAccessTokens.size(); i++) {
            OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(oauthAccessTokens.get(i).getClientId());
            String logoutUrl = oauthClientInfo.getLogoutUri();
            try {
                clientRemote.logoutClient(logoutUrl, oauthAccessTokens.get(i).getAccessToken());
            } catch (Exception e) {
                // 可以发邮件通知失败
                e.printStackTrace();
            }
        }
        return ResponseResult.success(true);
    }

}