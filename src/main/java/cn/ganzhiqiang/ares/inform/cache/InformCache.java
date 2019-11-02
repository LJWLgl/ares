package cn.ganzhiqiang.ares.inform.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

/**
 * @author nanxuan
 * @since 2018/5/13
 **/

@Repository
public class InformCache {

  @Resource
  private StringRedisTemplate redisCache;

  public void put(String pattern, String key, String value, long ttl) {
    redisCache.opsForValue().set(generateKey(pattern, key), value, ttl, TimeUnit.MILLISECONDS);
  }

  public String get(String pattern, String key) {
    return redisCache.opsForValue().get(generateKey(pattern, key));
  }

  public int getCount(String pattern, String key) {
    String count = redisCache.opsForValue().get(generateKey(pattern, key));
    if (StringUtils.isEmpty(count)) {
      return 0;
    }
    return Integer.parseInt(count);
  }

  public void inCrCount(String pattern, String key, long ttl) {
    String count = redisCache.opsForValue().get(generateKey(pattern, key));
    if (count == null) {
      redisCache.opsForValue().set(generateKey(pattern, key), "0", ttl, TimeUnit.MILLISECONDS);
    }
    redisCache.opsForValue().increment(generateKey(pattern, key), 1);
  }

  private String generateKey(String pattern, String key) {
    return String.format(pattern, key);
  }

}
