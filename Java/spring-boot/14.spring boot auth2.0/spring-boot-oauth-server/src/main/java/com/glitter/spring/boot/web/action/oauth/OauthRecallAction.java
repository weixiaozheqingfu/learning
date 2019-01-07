package com.glitter.spring.boot.web.action.oauth;

import com.glitter.spring.boot.bean.UserAuthorizationInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IUserInfoAuthorizationService;
import com.glitter.spring.boot.service.IUserInfoService;
import com.glitter.spring.boot.web.action.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/oauth2/recall")
public class OauthRecallAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthRecallAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    @Autowired
    private IUserInfoAuthorizationService userInfoAuthorizationService;

    /**
     * 获取用户已授权列表
     * @param userId 用户id
     * @return
     */
    public ResponseResult<List<UserAuthorizationInfo>> getUserAuthorizationInfos(@RequestParam(required = false) Long userId) {
        ResponseResult<List<UserAuthorizationInfo>> result = null;
        if (null == userId) {
            return result;
        }

        // 查询用户已授权的且refreshToken未过期的授权记录
        List<UserAuthorizationInfo> userAuthorizationInfos = userInfoAuthorizationService.getUserAuthorizationInfosByUserId(userId);
        result.setData(userAuthorizationInfos);
        return result;
    }

    /**
     * 解除授权方法
     * @param id 授权主键
     * @return
     */
    public ResponseResult recallUserAuthorization(@RequestParam(required = false) Long id) {
        ResponseResult<UserAuthorizationInfo> result = null;
        if (null == id) {
            throw new BusinessException("-1", "参数异常");
        }
        userInfoAuthorizationService.recallUserAuthorization(id);
        return result;
    }

}