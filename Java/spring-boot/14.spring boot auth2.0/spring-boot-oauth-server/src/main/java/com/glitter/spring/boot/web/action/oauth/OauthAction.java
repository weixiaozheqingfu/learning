package com.glitter.spring.boot.web.action.oauth;

import com.glitter.spring.boot.bean.*;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.*;
import com.glitter.spring.boot.web.action.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/oauth2")
public class OauthAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthAction.class);

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthCodeService oauthCodeService;

    @Autowired
    private IOauthScopeEnumService oauthScopeEnumService;

    @Autowired
    private IAccessToken4AuthorizationCodeService accessToken4AuthorizationCodeService;

    @Autowired
    private IAccessToken4RefreshTokenService accessToken4RefreshTokenService;

    /**
     * 示例: http://localhost:8080/oauth2/authorize?client_id=1001&redirect_uri=https://gitee.com/auth/csdn/callback&state=sdf8gswer9t89fdb8s9fg8r
     */
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String authorize(Model model,
                            @RequestParam(required = false) String client_id,
                            @RequestParam(required = false) String redirect_uri,
                            @RequestParam(required = false) String scope,
                            @RequestParam(required = false) String response_type,
                            @RequestParam(required = false) String state) {

        if (StringUtils.isBlank(client_id)) {
            model.addAttribute("errorMsg", "clientId参数错误");
            return "/error";
        }
        if (StringUtils.isBlank(redirect_uri)) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if (null == oauthClientInfo) {
            model.addAttribute("errorMsg", "clientId参数错误");
            return "/error";
        }
        String redirect_uri_decode;
        try {
            redirect_uri_decode = URLDecoder.decode(redirect_uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        if (!oauthClientInfo.getRedirectUri().equals(redirect_uri) && !oauthClientInfo.getRedirectUri().equals(redirect_uri_decode)) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        if (StringUtils.isBlank(scope)) {
            scope = "get_user_open_info";
        } else {
            List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumService.getAll();
            List<String> scopeNames = oauthScopeEnums.stream().map(OauthScopeEnum::getScopeName).distinct().collect(Collectors.toList());
            List<String> scopes = Arrays.asList(scope.split(","));
            if (!scopeNames.containsAll(scopes)) {
                model.addAttribute("errorMsg", "scope参数错误");
                return "/error";
            }
        }
        if (StringUtils.isBlank(response_type)) {
            response_type = GlitterConstants.OAUTH_RESPONSE_TYPE_CODE;
        }

        // 准备scopes数据
        List<OauthScopeEnum> scopes = oauthScopeEnumService.getByScopeNames(Arrays.asList(scope.split(",")));
        model.addAttribute("scopes", scopes);

        // 检查用户是否已处于登陆状态
        UserInfo userInfo = (UserInfo) sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
        model.addAttribute("userId", null == userInfo ? null : userInfo.getId());
        model.addAttribute("fullName", null == userInfo ? null : userInfo.getFullName());

        model.addAttribute("client_id", client_id);
        model.addAttribute("redirect_uri", redirect_uri);
        model.addAttribute("scope", scope);
        model.addAttribute("response_type", response_type);
        model.addAttribute("state", state);
        return "/authorize";
    }


    /**
     * 预授权方法:该方法不可跨域访问
     * <p>
     * 验证用户名密码后，有几种处理方案，
     * 1.该授权登录和服务器正常登陆域名一致，cookie写入的域名和服务器端的session对象生成完全用一套，授权后，也相当于登陆了该服务器系统，会往客户端写cookie。
     * 这样下次认证时，如果已经登陆过了且没有过期，可以不用再次输入用户名和密码。
     * 2.该授权登录和服务器正常登陆域名分开，cookie写入的域名和服务器端的session对象生成各自用一套，授权后，只是登陆了该授权系统，会往客户端对应的授权域名写cookie。
     * 这样下次认证时，如果已经在授权服务器登陆过了且没有过期，可以不用再次输入用户名和密码。
     * 3.不论是上面的方案1还是方案2,都专门为授权做一次单独的用户名密码的认证,没有任何cookie和服务器会话对象相关内容,做纯粹的授权时候使用的用户身份信息验证。
     * <p>
     * 建议使用第二种,通常情况下,这里是没有图形验证码的,与正常登陆混在一块不太安全,而如果不进行登陆记录用户状态,用户重复登陆时,还需要再次输入用户名密码
     * 这种一下,就采用第二种方案了,并且通常情况这些大站点都是采用第二种方式,比如qq,csdn等,他们都有对外开发api的单独域名,当然也有采用第一种方式的,比如github.
     * 本地模拟由于模拟多域名比较麻烦,所以就采用第一种方式。
     *
     * @param model
     * @param client_id
     * @param redirect_uri
     * @param scope
     * @param response_type
     * @param state
     * @param u
     * @param p
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "authorize", method = RequestMethod.POST)
    public ResponseResult<String> authorize(Model model,
                                            @RequestParam(required = false) String client_id,
                                            @RequestParam(required = false) String redirect_uri,
                                            @RequestParam(required = false) String scope,
                                            @RequestParam(required = false) String response_type,
                                            @RequestParam(required = false) String state,
                                            @RequestParam(required = false) String u,
                                            @RequestParam(required = false) String p) {
        // 1.验证auth参数最基本的合法性
        if (StringUtils.isBlank(client_id)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (StringUtils.isBlank(redirect_uri)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(client_id);
        if (null == oauthClientInfo) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        String redirect_uri_decode;
        try {
            redirect_uri_decode = URLDecoder.decode(redirect_uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (!oauthClientInfo.getRedirectUri().equals(redirect_uri) && !oauthClientInfo.getRedirectUri().equals(redirect_uri_decode)) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        if (StringUtils.isBlank(scope)) {
            scope = "get_user_open_info";
        } else {
            List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumService.getAll();
            List<String> scopeNames = oauthScopeEnums.stream().map(OauthScopeEnum::getScopeName).distinct().collect(Collectors.toList());
            List<String> scopes = Arrays.asList(scope.split(","));
            if (!scopeNames.containsAll(scopes)) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
            }
        }
        if (StringUtils.isBlank(response_type)) {
            response_type = GlitterConstants.OAUTH_RESPONSE_TYPE_CODE;
        }

        // 2.验证用户身份
        UserInfo userInfo;
        if (StringUtils.isNotBlank(u) && StringUtils.isNotBlank(p)) {
            userInfo = userInfoService.getUserInfoByAccount(u);
            if (null == userInfo || !userInfo.getPassword().equals(p)) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "用户名或密码错误");
            }
            sessionHandler.getSession().setAttribute(GlitterConstants.SESSION_USER, userInfo);
        } else {
            userInfo = (UserInfo) sessionHandler.getSession().getAttribute(GlitterConstants.SESSION_USER);
            if (null == userInfo) {
                throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "会话已过期,请重新登陆并授权");
            }
        }

        // 3.生成code码
        String code = "";
        // 目前就支持code模式
        if (response_type.equals("code")) {
            OauthCode oauthCode = new OauthCode();
            oauthCode.setUserId(userInfo.getId());
            oauthCode.setClientId(client_id);
            oauthCode.setScope(scope);
            code = oauthCodeService.generateCode(oauthCode);
        }

        // 4.生成回调地址
        String callbackUrl;
        try {
            String redirectUriDecode = URLDecoder.decode(oauthClientInfo.getRedirectUri(), "UTF-8");
            if (redirectUriDecode.indexOf("?") > 0) {
                callbackUrl = redirectUriDecode + "&code=" + code + "&state=" + state;
            } else {
                callbackUrl = redirectUriDecode + "?code=" + code + "&state=" + state;
            }
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "连接失败");
        }
        return ResponseResult.success(callbackUrl);
    }


    /**
     * 请求示例:
     * http://localhost:8080/oauth2/access_token?client_id=1001&client_secret=123456&redirect_uri=https://gitee.com/auth/csdn/callback&code=sdf8gsweb8s9fg8r&grant_type=authorization_code
     * <p>
     * 正确返回样例：
     * {
     * "access_token":"ACCESS_TOKEN",
     * "token_type":"bearer",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "scope":"SCOPE",
     * "openid":"OPENID"
     * }
     * <p>
     * 错误返回样例：
     * {
     * "errcode":40029,
     * "errmsg":"invalid code"
     * }
     */
    @ResponseBody
    @RequestMapping(value = "access_token", method = RequestMethod.GET)
    public Map<String, Object> getAccessToken(@RequestParam(required = false) String client_id,
                                              @RequestParam(required = false) String client_secret,
                                              @RequestParam(required = false) String redirect_uri,
                                              @RequestParam(required = false) String code,
                                              @RequestParam(required = false) String grant_type) {
        Map resultMap = new HashMap<>();
        Map errorMap = new HashMap<>();
        try {
            // grant_type的不同会有不同的实现类,这里演示的是grant_type=authorization_code授权码模式根据code换取accessToken的情况.
            if (GlitterConstants.OAUTH_GRANT_TYPE_AUTHORIZATION_CODE.equals(grant_type)) {
                AccessTokenOuter accessTokenInfo = accessToken4AuthorizationCodeService.getAccessTokenInfo(client_id, client_secret, redirect_uri, code, grant_type);
                if (null == accessTokenInfo) {
                    errorMap.put("errcode", CoreConstants.REQUEST_PROGRAM_ERROR_CODE);
                    errorMap.put("errmsg", "系统异常");
                    return errorMap;
                }
                resultMap.put("access_token", accessTokenInfo.getAccess_token());
                resultMap.put("token_type", accessTokenInfo.getToken_type());
                resultMap.put("expires_in", accessTokenInfo.getExpires_in());
                resultMap.put("refresh_token", accessTokenInfo.getRefresh_token());
                resultMap.put("scope", accessTokenInfo.getScope());
                resultMap.put("openid", accessTokenInfo.getOpenid());
            } else if (GlitterConstants.OAUTH_GRANT_TYPE_PASSWORD.equals(grant_type)) {
                // ......
            } else {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "grant_type参数值非法");
                return errorMap;
            }
            return resultMap;
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            errorMap.put("errcode", ex.getCode());
            errorMap.put("errmsg", ex.getMessage());
            return errorMap;
        }
    }


    /**
     * 检验授权凭证（access_token）是否有效
     * <p>
     * 正确返回样例：
     * {
     * "errcode":0,
     * "errmsg":"ok"
     * }
     * <p>
     * 错误返回样例：
     * {
     * "errcode":40003,
     * "errmsg":"invalid openid"
     * }
     *
     * @param access_token
     * @param openid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public Map<String, Object> auth(@RequestParam(required = false) String access_token,
                                    @RequestParam(required = false) String openid) {
        // TODO
        return null;
    }


    /**
     * 正常情况下应该在整个授权服务器中总共支持5种获取accessToken的模式,并且都是调用access_token方法即可,只是grant_type参数值不同,会走不同的模式,
     * 这5种模式如下：
     * 1.授权码模式     grant_type=authorization_code 有请求服务器端用户授权页环节,且response_type=code,服务器端页面提交时,发现response_type=token,会请求authorize方法,给客户端生成预授权码
     * 2.简化模式      grant_type=implicit  有请求服务器端用户授权页环节,且response_type=token,服务器端页面提交时,发现response_type=token,会直接请求access_token方法,并会设置grant_type=implicit
     * 3.密码模式      grant_type=password
     * 4.客户端模式    grant_type=client_credentials
     * 5.更新令牌模式  grant_type=refresh_token
     * <p>
     * 有这么多种模式,而我们定义的方法只有authorize(GET授权页),authorize(POST授权页提交,授权码模式下使用),access_token这三个方法就足够满足需求了,尤其是access_token方法,要同时支持5中模式,
     * 那么我们只要在access_token方法中定义出来所有模式都需要的参数,只要客户端调用该方法时,grant_type传不同的类型,然后传入对应类型所需要传入的参数就可以了,理论上是这样的,
     * 但是理想很丰满,现实很骨感,设想很完美,现实未必如此,尤其是代码界,存在很多过度设计问题,有一些设计很鸡肋,虽然设计上可行,但是使用方经过多方考量,往往会放弃这些鸡肋方案,从而考虑出更符合实际可行的方案来.
     * <p>
     * 具体到这里,我们说oauth的产生解决的根本问题是安全问题,是基于用户授权临时安全问题,安全是最重要的,从这个角度看,上面的几种模式都达到这个效果了吗?
     * 1.授权码模式,毫无疑问,该模式达到了安全的目的.
     * <p>
     * 2.简化模式,该模式会经过用户授权,但是存在两方面安全问题,
     * （1）客户端没有输入客户端秘钥,资源服务器无法确认该请求是合法客户端发起的,同时从客户端角度说,如果有恶意的另外一个客户端冒用该客户单的client_id
     * 发起请求授权页,那么用户输入用户密码后,会直接回调该客户端,那么如果客户端没有完备的state参数方案的话,可能就到了其客户端首页了,会展示出用户在该客户端的相关信息.
     * （2）.该模式会直接将access_token以重定向的方式返回给客户端,客户端在浏览器就可以明文看到该access_token值,这是一个安全隐患.
     * <p>
     * 3.密码模式,需要用户在客户端页面输入用户名密码,然后客户端将用户名密码提交给授权服务器,密码太容易泄露给第三方了,是否会泄露只取决于第三方的人品。
     * <p>
     * 4.客户端模式,该模式跟用户授权没有一毛钱关系了,严格来说应该部署与oauth授权范围了,应该自立门户,因为该授权完全是客户端系统是否有访问服务器端的接口权限,是系统与系统之间的权限,跟用户授权
     * 没有一毛钱关系,用户也没有参与进来,属于用户在使用一个客户端系统时,客户端系统需要访问授权服务器的某些资源,那么客户系统需要提前在授权服务器进行应用注册,服务器需要单独为此做独立的设计,这属于是系统之间的调用约定.
     * 对于授权服务器来说,总不能随便来一个客户端都能访问我的接口,只有提前在我系统中注册的三方应用,可以固定的访问我的某些接口.
     * 客户端模式通常使用在互相可信的企业内部系统之间,解决大系统内部的系统之间服务之间的安全调用问题.
     * <p>
     * 5.更新令牌模式,这没有什么安全问题,上面的几种模式都可以生成refresh_token,以便在access_token过期时方便的换取,但通常情况下,大家简化模式并不生成refresh_token.
     * <p>
     * <p>
     * 并且,授权码模式配合更新令牌模式基本能满足90%的场景,剩下10%的场景,客户端模式也能满足了。简化模式和密码模式模式的存在就是鸡肋,怎么可能出现一个客户端没有对应的后台服务呢,反正我目前是没有接触到,也很难想象。
     * 并且密码模式简直令人发指,完全背离了oauth的初衷啊,对于设计者也是,这完全是自相矛盾的设计啊,怎么可能将用户名密码交由第三方客户端的页面来进行输入呢,用户的用户名密码都相当于直接告诉第三方了,还说什么临时授权呢？
     * 当然我冷静下来又反复想了想,这种模式可能适用于企业内部的多个子系统,把子系统作为一个客户端,这时候用户在不同的子系统输入用户名密码换取access_token倒也说的过去。
     * 还有说密码模式作用于andoid或ios等非浏览器客户端,我想说的是这完全是借口,请问微信是如何做的,扫码啊亲,扫码,既创造了浏览器环境又有非常好的用户体验,一切问题都会有其更合理的解决办法.
     * 而至于简化模式,上面两点也提到了并不安全,我实在没想到会有一段客户端代码没有对应的服务器端,只能走简化模式而不能走授权码模式的情况,在我看来简化模式根本就应该废弃,都走标准的授权码模式就好了。
     * <p>
     * 上面说了这么多,回归一下主题,按照上面的分析,其实我们还是只需要三个方法就好了,即authorize(GET授权页),authorize(POST授权页提交,授权码模式下使用),access_token这三个方法,
     * 但是这里我不想这么设计,因为真正对我们有用的获取access_token的模式只有三种授权码模式\客户端模式\更新令牌模式,其中更新令牌模式可以自立门户了,不多说了,这里其实就剩下授权码模式\更新令牌模式这两种模式了,
     * 这种情况下,为了方便和明确,其实我们可以将这两种模式分别用两个方法来做,而不是继续共用access_token这一个方法.
     * 其实就连微信的对外开放平台也是这么做的,并且微信也只支持授权码这一种模式外加更新令牌模式.微信的access_token专指授权码模式换取的access_token.
     * 我们为什么不呢？
     *
     * @param client_id
     * @param refresh_token
     * @param grant_type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "refresh_token", method = RequestMethod.GET)
    public Map<String, Object> refresh_token(@RequestParam(required = false) String client_id,
                                             @RequestParam(required = false) String refresh_token,
                                             @RequestParam(required = false) String grant_type) {
        Map resultMap = new HashMap<>();
        Map errorMap = new HashMap<>();
        try {
            if (!GlitterConstants.OAUTH_GRANT_TYPE_REFRESH_TOKEN.equals(grant_type)) {
                errorMap.put("errcode", CoreConstants.REQUEST_ERROR_PARAMS);
                errorMap.put("errmsg", "grant_type参数非法");
                return errorMap;
            }

            AccessTokenOuter accessTokenInfo = accessToken4RefreshTokenService.getAccessTokenInfo(client_id, refresh_token, grant_type);
            if (null == accessTokenInfo) {
                errorMap.put("errcode", CoreConstants.REQUEST_PROGRAM_ERROR_CODE);
                errorMap.put("errmsg", "系统异常");
                return errorMap;
            }
            resultMap.put("access_token", accessTokenInfo.getAccess_token());
            resultMap.put("token_type", accessTokenInfo.getToken_type());
            resultMap.put("expires_in", accessTokenInfo.getExpires_in());
            resultMap.put("refresh_token", accessTokenInfo.getRefresh_token());
            resultMap.put("scope", accessTokenInfo.getScope());
            resultMap.put("openid", accessTokenInfo.getOpenid());

            return resultMap;
        } catch (Exception e) {
            BusinessException ex = (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
            errorMap.put("errcode", ex.getCode());
            errorMap.put("errmsg", ex.getMessage());
            return errorMap;
        }
    }

}