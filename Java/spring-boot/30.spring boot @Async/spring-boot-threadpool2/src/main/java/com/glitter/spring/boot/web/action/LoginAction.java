package com.glitter.spring.boot.web.action;

import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.service.IRsaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginAction {

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Autowired
    private IRsaService rsaService;

    /**
     * 获取公钥
     * @return
     */
    @RequestMapping(value = "getPublicKey", method = RequestMethod.GET)
    public ResponseResult<String> getPublicKey() {
        int i = 3/0;
        String publicKey = rsaService.getPublicKey();
        return ResponseResult.success(publicKey);
    }

}

