package com.glitter.spring.boot.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/oauth2")
public class OauthAction extends BaseAction{

    private static final Logger logger = LoggerFactory.getLogger(OauthAction.class);

    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String deleteById(Model model, @RequestParam String clientId) {
        model.addAttribute("clientId", clientId);
        return "/authorize";
    }

}