package com.glitter.spring.boot.web;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/excute")
public class ExcuteAction {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfo(@RequestParam Long id){
        UserInfo userInfo = new UserInfo();
        userInfo.setName("张三丰");
        userInfo.setAge(100);

//        redisTemplate.execute((RedisCallback<Void>) con -> {
//            String key = "redis:test:userInfo";
//            String value = JSONObject.toJSONString(userInfo);
//            con.set(key.getBytes(), value.getBytes());
//            return null;
//        });

//        redisTemplate.execute((RedisConnection con) -> {
//            String key = "redis:test:userInfo";
//            String value = JSONObject.toJSONString(userInfo);
//            con.set(key.getBytes(), value.getBytes());
//            return null;
//        });

//        redisTemplate.execute((RedisCallback<Void>) (RedisConnection con) -> {
//            String key = "redis:test:userInfo";
//            String value = JSONObject.toJSONString(userInfo);
//            con.set(key.getBytes(), value.getBytes());
//            return null;
//        });

//        String key = "redis:test:userInfo";
//        String asn = (String)redisTemplate.execute((RedisCallback<String>) con -> con.get(key.getBytes()) == null ? null : new String(con.get(key.getBytes())
//        ));
//        System.out.println(asn);


//        String hkey = "hKey";
//        redisTemplate.execute((RedisCallback<Void>) con -> {
//            con.hSet(hkey.getBytes(), "key1".getBytes(), "what".getBytes());
//            con.hSet(hkey.getBytes(), "key2".getBytes(), "is".getBytes());
//            con.hSet(hkey.getBytes(), "key3".getBytes(), "your".getBytes());
//            con.hSet(hkey.getBytes(), "key4".getBytes(), "name".getBytes());
//            con.hSet(hkey.getBytes(), "key5".getBytes(), "?".getBytes());
//            return null;
//        });

//        Map<byte[], byte[]> map = (Map<byte[], byte[]>) redisTemplate.execute((RedisCallback<Map<byte[], byte[]>>) con -> con.hGetAll(hkey.getBytes()));
//        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
//            System.out.println("key: " + new String(entry.getKey()) + " | value: " + new String(entry.getValue()));
//        }

//        Boolean res = (Boolean)redisTemplate.execute((RedisCallback<Boolean>) (RedisConnection connection) -> {
//            RedisSerializer<String> serializerKey = redisTemplate.getStringSerializer();
//            RedisSerializer<UserInfo> serializerValue = redisTemplate.getValueSerializer();
//            byte[] transKey = serializerKey.serialize("redis:test:userInfo");
//            byte[] transValue = serializerValue.serialize(userInfo);
//            return connection.setNX(transKey, transValue);
//        });

        Boolean res = (Boolean)redisTemplate.execute((RedisCallback<Boolean>) (RedisConnection connection) -> {
            // 这种底层的序列化策略的生命周期仅限于本方法,不会影响全局的redisTemplate的序列化策略的变化,
            // 但是如果这里使用redisTemplate进行序列化策略变化,那则是全局的变化了,因为redisTemplate对象是单例的。
//            redisTemplate.setKeySerializer(new StringRedisSerializer());
//            redisTemplate.setValueSerializer(new StringRedisSerializer());
            RedisSerializer<String> serializerKey = redisTemplate.getStringSerializer();
            RedisSerializer<String> serializerValue = redisTemplate.getStringSerializer();
            byte[] transKey = serializerKey.serialize("redis:test:userInfo");
            byte[] transValue = serializerValue.serialize(JSONObject.toJSONString(userInfo));
            return connection.setNX(transKey, transValue);
        });

        return ResponseResult.success(userInfo);
    }

}
