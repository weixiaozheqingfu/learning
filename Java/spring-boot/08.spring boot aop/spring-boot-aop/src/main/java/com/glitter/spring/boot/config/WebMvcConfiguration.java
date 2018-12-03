package com.glitter.spring.boot.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * json序列化
     *
     * 好像没有作用,再研究吧
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig converterConfig = new FastJsonConfig();
        converterConfig.setSerializerFeatures(PrettyFormat, WriteMapNullValue, WriteNullListAsEmpty, WriteNullStringAsEmpty);
        converter.setFastJsonConfig(converterConfig);
        converters.add(converter);
    }

}
