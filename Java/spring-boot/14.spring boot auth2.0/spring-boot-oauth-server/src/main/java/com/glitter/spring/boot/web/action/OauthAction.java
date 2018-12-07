package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.OauthScopeEnum;
import com.glitter.spring.boot.service.IOauthClientInfoService;
import com.glitter.spring.boot.service.IOauthScopeEnumService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/oauth2")
public class OauthAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthAction.class);

    @Autowired
    private IOauthClientInfoService oauthClientInfoService;

    @Autowired
    private IOauthScopeEnumService oauthScopeEnumService;

    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String authorize(Model model,
                            @RequestParam(required = false) String clientId,
                            @RequestParam(required = false) String redirect_uri,
                            @RequestParam(required = false) String scope,
                            @RequestParam(required = false) String response_type,
                            @RequestParam(required = false) String state) {

        if(StringUtils.isBlank(clientId)) {
            model.addAttribute("errorMsg", "clientId参数错误");
            return "/error";
        }
        if(StringUtils.isBlank(redirect_uri)) {
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/authorize";
        }
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getOauthClientInfoByClientId(clientId);
        if(null == oauthClientInfo){
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
        if(!oauthClientInfo.getRedirectUri().equals(redirect_uri) && !oauthClientInfo.getRedirectUri().equals(redirect_uri_decode)){
            model.addAttribute("errorMsg", "redirect_uri参数错误");
            return "/error";
        }
        if(StringUtils.isBlank(scope)){
            scope = "get_user_open_info";
        }else{
            List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumService.getAllOauthScopeEnums();
            List<String> scopeNames = oauthScopeEnums.stream().map(OauthScopeEnum::getScopeName).distinct().collect(Collectors.toList());
            List<String> scopes = Arrays.asList(scope.split(","));
            if(!scopeNames.containsAll(scopes)){
                model.addAttribute("errorMsg", "scope参数错误");
                return "/error";
            }
        }
        if(StringUtils.isBlank(response_type)){
            response_type = "code";
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("redirect_uri", redirect_uri);
        model.addAttribute("scope", scope);
        model.addAttribute("response_type", response_type);
        model.addAttribute("state", state);
        return "/authorize";
    }

    // 授权方法
    // 验证用户名密码后，有几种处理方案，
    // 1.该授权登录和服务器正常登陆域名一致，cookie写入的域名和服务器端的session对象生成完全用一套，授权后，也相当于登陆了该服务器系统，会往客户端写cookie。
    //   这样下次认证时，如果已经登陆过了且没有过期，可以不用再次输入用户名和密码。
    // 2.该授权登录和服务器正常登陆域名分开，cookie写入的域名和服务器端的session对象生成各自用一套，授权后，只是登陆了该授权系统，会往客户端对应的授权域名写cookie。
    //   这样下次认证时，如果已经在授权服务器登陆过了且没有过期，可以不用再次输入用户名和密码。
    // 3.不论是上面的方案1还是方案2,都专门为授权做一次单独的用户名密码的认证,没有任何cookie和服务器会话对象相关内容,做纯粹的授权时候使用的用户身份信息验证。

}