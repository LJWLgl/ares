package cn.ganzhiqiang.ares.common;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 计数服务的抽象类
 * @author nanxuan
 * @since 2018/2/19
 **/

public abstract class AbstractCounter {

  protected static final String COUNTER_HASH_PATTERN = "count:%s.%s";

  protected static final String COUNTER_PATTERN = "count:%s";

  protected static final long DEFAULT_COUNT = 0;

  protected abstract StringRedisTemplate getCounter();

  protected long findCount(Object id, String domain, String field) {
    String key = generateKey(id, domain);
    Object count = getCounter().opsForHash().get(key, field);
    return count == null ? 0L : Long.parseLong(String.valueOf(count));
  }

  // no test
  protected <K> Map<K, Long> queryCount(List<K> ids, String domain, String field) {
    Map<K, String> keyMap = new HashMap<>();
    List<String> keys = new ArrayList<>();
    for (K id : ids) {
      keyMap.put(id, generateKey(id, domain));
      keys.add(generateKey(id, domain));
    }
    if (CollectionUtils.isEmpty(keys)) {
      return new HashMap<>();
    }
    Map<String, Long> countMap = getMultiKeyCount(keys, field);
    Map<K, Long> replyMap = new HashMap<>();
    for (K id : ids) {
      String key = keyMap.get(id);
      if (key == null) {
        replyMap.put(id, DEFAULT_COUNT);
        continue;
      }
      Long count = countMap.get(key);
      if (count == null) {
        count = DEFAULT_COUNT;
      }
      replyMap.put(id, count);
    }

    return replyMap;
  }

  private Map<String, Long> getMultiKeyCount(final List<String> keys, final String field) {
    Map<String, Long> res = new HashMap<>();
    List<Object> pips = getCounter().executePipelined(new RedisCallback<Object>() {
      @Override
      public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
        StringRedisConnection stringRedisConn = (StringRedisConnection)redisConnection;
        for (String key : keys) {
          stringRedisConn.hGet(key, field);
        }
        return null;
      }
    });
    int index = 0;
    if (pips != null) {
      for (Iterator iterator = keys.iterator(); iterator.hasNext(); index++) {
        String key = String.valueOf(iterator.next());
        Object pipeResult = pips.get(index);
        Long count = pipeResult == null ? 0L : Long.parseLong(String.valueOf(pipeResult));
        res.put(key, count);
      }
    }
    return res;
  }

  protected long incrCount(String id, String domain, String field, int step) {
    String key = generateKey(id, domain);
    return getCounter().opsForHash().increment(key, field, step);
  }

  protected long decrCount(String id , String domain, String field , int step) {
    String key = generateKey(id, domain);
    return getCounter().opsForHash().increment(key, field, -step);
  }


  protected String generateKey(Object id, String domain) {
    return String.format(COUNTER_HASH_PATTERN, domain, id);
  }

}
