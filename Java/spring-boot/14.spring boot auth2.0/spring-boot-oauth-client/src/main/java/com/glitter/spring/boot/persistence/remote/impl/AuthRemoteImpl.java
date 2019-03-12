package com.glitter.spring.boot.persistence.remote.impl;

import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.TypeReference;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.remote.IAuthRemote;
import com.glitter.spring.boot.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Service
public class AuthRemoteImpl implements IAuthRemote {

	private static final String getMemberByIds="/inner/member/getMemberByIds";

	private static final Logger logger = LoggerFactory.getLogger(AuthRemoteImpl.class);

	@Autowired
	private RestTemplateUtils restTemplateUtils;

	@Override
	public void getOauthServerAccessToken(String memberIds) {
		try {
			if(StringUtils.isBlank(memberIds)){

			}

			Map<String, Object> param = new HashMap<>(1);
			param.put("memberIds", memberIds);
			String url = "http://localhost:8080/" + getMemberByIds;
			String json = restTemplateUtils.getFormRequest(url, param);

			// TODO 结果转map
			ResponseResult<List<?>> responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult<List<?>>>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}
//			return responseResult.getData();
		} catch (Exception e) {
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

}
