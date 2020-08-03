package cn.ganzhiqiang.ares.common.helper;

import cn.ganzhiqiang.ares.common.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zq_gan
 * @since 2019/10/26
 **/

@Component
@PropertySource(value = {"file:" + CommonConstants.DEFAULT_CONFIG_PATH})
public class GConfigHelper {

    @Resource
    private Environment env;

    public <T> T getValueByKey(String key, Class<T> cls) {
        return getValueByKey(key, cls, null);
    }

    public <T> T getValueByKey(String key, Class<T> cls, T defaultValue) {
        if (StringUtils.isBlank(key)) {
            return defaultValue;
        }
        T result = env.getProperty(key, cls);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }


}

