package com.glitter.spring.boot.web;

import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoAction{

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseResult add(@RequestBody UserInfo bean){
        System.out.println(bean.toString());
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseResult delete(@RequestParam Long id){
        System.out.println(id);
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResponseResult modify(@RequestBody UserInfo bean){
        System.out.println(bean.toString());
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public ResponseResult<UserInfo> getUserInfo(@RequestParam Long id){
        System.out.println();
        return ResponseResult.success(null);
    }

    @RequestMapping(value = "/getUserInfos", method = RequestMethod.POST)
    public ResponseResult<List<UserInfo>> getUserInfos(@RequestParam Long id){
        System.out.println();
        return ResponseResult.success(null);
    }

}
