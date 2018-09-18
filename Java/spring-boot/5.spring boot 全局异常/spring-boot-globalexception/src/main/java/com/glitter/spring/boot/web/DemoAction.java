package com.glitter.spring.boot.web;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.exception.BusinessException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoAction{

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult add(@RequestBody UserInfo bean){
        System.out.println(JSONObject.toJSONString(bean));
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseResult delete(@RequestParam Long id){
        System.out.println(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseResult modify(@RequestBody UserInfo bean){
        System.out.println(JSONObject.toJSONString(bean));
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfo(@RequestParam Long id){
        if(id<0){
            throw new BusinessException("100101", "id值非法");
        }
        if(id==0){
            throw new NullPointerException();
        }
        System.out.println(id);
        UserInfo userInfo = new UserInfo();
        userInfo.setName("张三丰");
        userInfo.setAge(100);
        System.out.println(JSONObject.toJSONString(userInfo));
        return ResponseResult.success(userInfo);
    }

    @RequestMapping(value = "/getUserInfos", method = RequestMethod.GET)
    public ResponseResult<List<UserInfo>> getUserInfos(@RequestParam Long id){
        System.out.println(id);
        UserInfo userInfo = new UserInfo();
        userInfo.setName("张三丰");
        userInfo.setAge(100);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setName("李斯");
        userInfo1.setAge(50);
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(userInfo);
        userInfos.add(userInfo1);
        System.out.println(JSONObject.toJSONString(userInfos));
        return ResponseResult.success(userInfos);
    }

}
