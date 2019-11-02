package com.glitter.spring.boot.persistence.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.remote.IClientRemote;
import com.glitter.spring.boot.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientRemoteImpl implements IClientRemote {

	private static final Logger logger = LoggerFactory.getLogger(ClientRemoteImpl.class);

	private static final String getMemberByIds="/inner/member/getMemberByIds";

	@Autowired
	private RestTemplateUtils restTemplateUtils;

	@Override
	public void logoutClient(String url, String accessToken) throws Exception{
		try {
			if(StringUtils.isBlank(url)){
				throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "url参数为空");
			}
			if(StringUtils.isBlank(url)){
				throw new BusinessException(CoreConstants.REQUEST_ERROR_PARAMS, "jsessionidClient参数为空");
			}

			Map<String, Object> param = new HashMap<>(1);
			param.put("accessToken", accessToken);
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				logger.error("系统调用异常，调用地址:{}, 输入参数:{},返回json:{}", url, accessToken, json);
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
