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

}