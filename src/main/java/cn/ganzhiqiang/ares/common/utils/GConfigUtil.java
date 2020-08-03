package cn.ganzhiqiang.ares.common.utils;

import cn.ganzhiqiang.ares.common.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zq_gan
 * @since 2019/12/15
 **/

public class GConfigUtil {

    private static ConcurrentHashMap<String, Object> keyValueMap = new ConcurrentHashMap<>();

    public static <T> T getValue(String key, Class<T> clz) {
        return getValue(key, clz, null);
    }

    @SuppressWarnings(value = "unchecked")
    public static <T> T getValue(String key, Class<T> clz, T defaultValue) {
        if (StringUtils.isBlank(key)) {
            return defaultValue;
        }
        Object cacheValue = keyValueMap.get(key);
        if (cacheValue == null) {
            T value = FileUtil.getProperty(CommonConstants.DEFAULT_CONFIG_PATH, key, clz, null);
            if (value != null) {
                keyValueMap.put(key, value);
                return value;
            } else {
                return defaultValue;
            }
        } else {
            return (T) cacheValue;
        }
    }

}
