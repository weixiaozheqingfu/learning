package com.glitter.spring.boot.web;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoAction{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfo(@RequestParam Long id){

        redisTemplate.opsForValue().set("redis:test:1","1a里");
        String str = (String)redisTemplate.opsForValue().get("redis:test:1");
        System.out.println(str);

        stringRedisTemplate.opsForValue().set("redis:test:2","2a里");
        String str2 = stringRedisTemplate.opsForValue().get("redis:test:2");
        System.out.println(str2);

        UserInfo userInfo = new UserInfo();
        userInfo.setName("张三丰");
        userInfo.setAge(100);

        redisTemplate.opsForValue().set("redis:test:userInfo", JSONObject.toJSONString(userInfo));
        Object o = (String)redisTemplate.opsForValue().get("redis:test:userInfo");
        String userInfoStr = null == o ? null : String.valueOf(o);
        System.out.println(userInfoStr);

        redisTemplate.opsForValue().set("redis:test:userInfo", userInfo);
        UserInfo userInfo1 = (UserInfo)redisTemplate.opsForValue().get("redis:test:userInfo");
        System.out.println(userInfo1);

        return ResponseResult.success(userInfo);
    }

}
