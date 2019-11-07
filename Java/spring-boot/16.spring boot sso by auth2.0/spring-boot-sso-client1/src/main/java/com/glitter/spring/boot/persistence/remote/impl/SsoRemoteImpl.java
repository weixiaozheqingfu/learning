package com.glitter.spring.boot.persistence.remote.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.common.ResponseResult;
import com.glitter.spring.boot.constant.CoreConstants;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.remote.ISsoRemote;
import com.glitter.spring.boot.util.RestTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SsoRemoteImpl implements ISsoRemote {

	private static final Logger logger = LoggerFactory.getLogger(SsoRemoteImpl.class);

	@Autowired
	private RestTemplateUtils restTemplateUtils;

	@Override
	public Map getAccessToken(String client_id, String client_secret, String redirect_uri, String code, String grant_type) {
		try {
			Map<String, Object> param = new HashMap<>(1);
			param.put("client_id", client_id);
			param.put("client_secret", client_secret);
			param.put("redirect_uri", redirect_uri);
			param.put("code", code);
			param.put("grant_type", grant_type);

			String url = "http://localhost:8080/sso/access_token";
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult<Map> responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult<Map>>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}

			return responseResult.getData();
		} catch (Exception e) {
			logger.error(JSONObject.toJSONString(e));
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

	@Override
	public UserInfo getUerInfo(String access_token) {
		try {
			Map<String, Object> param = new HashMap<>(1);
			param.put("access_token", access_token);

			String url = "http://localhost:8080/sso/resource/userinfo";
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult<UserInfo> responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult<UserInfo>>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				logger.error(json);
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}
			return responseResult.getData();
		} catch (Exception e) {
			logger.error(JSONObject.toJSONString(e));
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

	@Override
	public void keepAlive(String access_token) {
		try {
			Map<String, Object> param = new HashMap<>(1);
			param.put("access_token", access_token);

			String url = "http://localhost:8080/sso/resource/keepAlive";
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}
		} catch (Exception e) {
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

	@Override
	public Map auth(String accessToken) {
		try {
			Map<String, Object> param = new HashMap<>(1);
			param.put("access_token", accessToken);

			String url = "http://localhost:8080/sso/auth";
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult<Map> responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult<Map>>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}
			return responseResult.getData();
		} catch (Exception e) {
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

	@Override
	public Map refreshToken(String client_id, String refresh_token, String grant_type) {
		try {
			Map<String, Object> param = new HashMap<>(1);
			param.put("client_id", client_id);
			param.put("refresh_token", refresh_token);
			param.put("grant_type", grant_type);

			String url = "http://localhost:8080/sso/refresh_token";
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult<Map> responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult<Map>>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}

			return responseResult.getData();
		} catch (Exception e) {
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

	@Override
	public void logout(String accessToken) {
		try {
			Map<String, Object> param = new HashMap<>(1);
			param.put("access_token", accessToken);

			String url = "http://localhost:8080/sso/resource/logout";
			String json = restTemplateUtils.getFormRequest(url, param);

			ResponseResult responseResult = JSONObject.parseObject(json, new TypeReference<ResponseResult>(){});
			if (responseResult == null || !responseResult.getCode().equals(CoreConstants.REQUEST_SUCCESS_CODE)) {
				throw new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统调用异常");
			}
		} catch (Exception e) {
			throw (e instanceof BusinessException) ? (BusinessException) e : new BusinessException(CoreConstants.REQUEST_PROGRAM_ERROR_CODE, "系统异常");
		}
	}

}
