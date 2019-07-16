package com.glitter.spring.boot.web;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.exception.BusinessException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aemo")
public class AemoAction {

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
        userInfo.setName("李斯");
        userInfo.setAge(100);
        System.out.println(JSONObject.toJSONString(userInfo));
        return ResponseResult.success(userInfo);
    }

}
