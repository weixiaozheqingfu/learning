package com.glitter.spring.boot.web;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hash")
public class HashAction {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfo(@RequestParam Long id){

        UserInfo userInfo = new UserInfo();
        userInfo.setName("张三丰");
        userInfo.setAge(100);

        redisTemplate.opsForHash().put("session","userInfo", userInfo);
        redisTemplate.opsForHash().put("session","smsVerificationCode", "1a2b");
        redisTemplate.opsForHash().put("session","GraphicVerificationCode", "123456");

        UserInfo userInfoCache = (UserInfo)redisTemplate.opsForHash().get("session","userInfo");
        String smsVerificationCode = (String)redisTemplate.opsForHash().get("session","smsVerificationCode");
        String GraphicVerificationCode = (String)redisTemplate.opsForHash().get("session","GraphicVerificationCode");

        System.out.println(userInfoCache);
        System.out.println(smsVerificationCode);
        System.out.println(GraphicVerificationCode);

        return ResponseResult.success(userInfo);
    }

}

