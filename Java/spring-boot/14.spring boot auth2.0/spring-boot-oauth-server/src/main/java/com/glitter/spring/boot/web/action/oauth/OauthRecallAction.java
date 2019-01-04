package com.glitter.spring.boot.web.action.oauth;

import com.glitter.spring.boot.bean.UserAuthorizationInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import com.glitter.spring.boot.service.IUserInfoService;
import com.glitter.spring.boot.web.action.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/oauth2/recall")
public class OauthRecallAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(OauthRecallAction.class);

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IOauthAccessTokenService oauthAccessTokenService;

    /**
     * 获取用户已授权列表
     * @param userId 用户id
     * @return
     */
    public ResponseResult<UserAuthorizationInfo> getUserAuthorizationInfos(@RequestParam(required = false) String userId) {
        ResponseResult<UserAuthorizationInfo> result = null;
        if (StringUtils.isBlank(userId)) {
            return result;
        }

        // TODO 专门的service
        // TODO 查询用户已授权的且refreshToken未过期的授权记录
        return result;
    }

    /**
     * 解除授权方法
     * @param id 授权主键
     * @return
     */
    public ResponseResult recallUserAuthorization(@RequestParam(required = false) String id) {
        ResponseResult<UserAuthorizationInfo> result = null;
        if (StringUtils.isBlank(id)) {
            throw new BusinessException("-1", "参数异常");
        }
        // TODO 专门的service delete操作
        return result;
    }
}