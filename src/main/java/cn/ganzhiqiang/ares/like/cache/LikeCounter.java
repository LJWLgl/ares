package cn.ganzhiqiang.ares.like.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.common.AbstractCounter;
import cn.ganzhiqiang.ares.common.CounterDomain;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/2/20
 **/

@Repository
public class LikeCounter extends AbstractCounter{

  private static final String LIKE_FIELD = "like";

  @Resource
  private StringRedisTemplate redisCounter;

  @Override
  protected StringRedisTemplate getCounter() {
    return redisCounter;
  }

  public void incrCount(Integer resourceId, CounterDomain domain) {
    incrCount(String.valueOf(resourceId), String.valueOf(domain.getValue()), LIKE_FIELD, 1);
  }

  public void decrCount(Integer resourceId, CounterDomain domain) {
    decrCount(String.valueOf(resourceId), String.valueOf(domain.getValue()), LIKE_FIELD, 1);
  }

  public long findCount(Integer resourceId, CounterDomain domain) {
    return findCount(resourceId, String.valueOf(domain.getValue()), LIKE_FIELD);
  }

  public Map<Integer, Long> queryCount(List<Integer> resourceIds, CounterDomain domain) {
    return queryCount(resourceIds, String.valueOf(domain.getValue()), LIKE_FIELD);
  }

}
