package cn.ganzhiqiang.ares.context;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author zqgan
 * @since 2019/3/31
 **/

@Configuration
public class AppConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 支持属性数量不完全一致
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 支持转义字符串序列化
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return objectMapper;
    }
}
