package com.glitter.spring.boot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestTemplateUtils {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateUtils.class);

    @Autowired
    private RestTemplate restTemplate;


    public String getFormRequest(String url, Map<String, Object> param) {
        if (logger.isInfoEnabled()) {
            logger.info("调用路径:{}，param参数：{}", url, JSON.toJSONString(param));
        }
        url = getUrl(url, param);

        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, null);
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                JSONObject.class, param);
        if (logger.isInfoEnabled()) {
            logger.info("返回结果：{}", response.getBody().toJSONString());
        }
        return response.getBody().toJSONString();
    }


    public String postBodyRequest(String url, Map<String, String> header, Map<String, Object> param, String bodyContent) {
        if (logger.isInfoEnabled()) {
            logger.info("调用路径:{}，header参数：{}，param参数：{}，bodyContent参数：{}", url, JSON.toJSONString(header), JSON.toJSONString(param), bodyContent);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 设置headers
        if (header != null && !header.isEmpty()) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                String value = entry.getValue();
                if (value != null && value.length() > 0) {
                    headers.add(entry.getKey(), value);
                }
            }
        }


        HttpEntity<String> requestEntity = new HttpEntity<>(bodyContent, headers);
        if (null != param) {
            url = getUrl(url, param);

            ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, requestEntity,
                    JSONObject.class, param);
            if (logger.isInfoEnabled()) {
                logger.info("返回结果：{}", response.getBody().toJSONString());
            }
            return response.getBody().toJSONString();

        }
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, requestEntity, JSONObject.class);
        if (logger.isInfoEnabled()) {
            logger.info("返回结果：{}", response.getBody().toJSONString());
        }
        return response.getBody().toJSONString();

    }

    private String getUrl(String url, Map<String, Object> param) {

        StringBuilder tempUrl = new StringBuilder();
        tempUrl.append(url);
        if (param != null && !param.isEmpty()) {
            tempUrl.append("?");
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    tempUrl.append(entry.getKey());
                    tempUrl.append("={");
                    tempUrl.append(entry.getKey());
                    tempUrl.append("}&");

                }
            }
        }
        return tempUrl.substring(0, tempUrl.length() - 1);
    }


}
