package com.glitter.spring.boot.service;

public interface IOauthAccessTokenService {


    /**
     * 验证accessToken,如果验证通过,则返回该accessToken对应的授权人openid,只有返回openid,本次的accessToken才是合法有效的
     *
     * 只要accessToken是合法的,就能说明当前的请求是一个合法客户端的请求,并且本次具体请求的interfaceUri地址也在accessToken的授权范围内,一切都在红线内,
     * 只是客户端请求的是服务器资源方,资源方是不知道当前的accessToken的对应的授权人信息的,需要将此信息返回给服务器资源方,服务器资源方在接口实现中就只能处理该授权人相关的数据,数据不能越界.
     *
     * (有的设计为了方便,在返回accessToken的时候同时返回了openid,然后调用资源方接口的时候,同时传入accessToken和openid,这种情况,资源法调用完validateAccessToken接口后
     * 需要进一步根据validateAccessToken接口返回的openid与客户端传入的openid对比是否一致,一致才能访问接口,否则失败.
     *  这种服务器资源方的接口设计也可以,他让客户端自己以参数的形式指明本次调用的接口要访问的数据范围即openid,然后服务器资源方接收到参数明白了当前客户端要访问这openid的数据,就看他的
     *  accessToken验证返回结果是不是这个openid,是的话说明没问题继续执行服务器接口逻辑并返回结果,反之,则客户端故意请求授权外的openid,企图蒙混过关,则服务器资源方进行异常抛出返回即可。
     * )
     *
     * @param accessToken
     * @param interfaceUri
     * @return
     */
    Long validateAccessToken(String accessToken, String interfaceUri);

    /**
     * 根据openid获取系统中对应的userId
     * @param openid
     * @return
     */
    Long getUserIdByOpenid(String openid);

    /**
     * 根据主键删除accessToken
     * @param id
     */
    void deleteById(Long id);

}