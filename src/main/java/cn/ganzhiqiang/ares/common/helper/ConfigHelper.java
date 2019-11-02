package cn.ganzhiqiang.ares.common.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.ganzhiqiang.ares.common.utils.FileUtil;

import javax.annotation.Resource;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zq_gan
 * @since 2019/10/26
 **/

@Component
@PropertySource(value = {"file:/gzq/config/ares/application.properties"})
public class ConfigHelper {

  private static final String DEFAULT_CONFIG_PATH = "/gzq/config/ares/application.properties";

  private static ConcurrentHashMap<String, Object> keyValueMap = new ConcurrentHashMap<>();

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

  @SuppressWarnings(value="unchecked")
  public static <T> T getValue(String key, Class<T> clz, T defaultValue) {
    if (StringUtils.isBlank(key)) {
      return defaultValue;
    }
    Object value = keyValueMap.get(key);
    if (value == null) {
      T value1  = FileUtil.getProperty(DEFAULT_CONFIG_PATH, key, clz, null);
      if (value1 != null) {
        keyValueMap.put(key, value1);
      }
      return value1;
    } else {
      return (T)value;
    }
  }

}

