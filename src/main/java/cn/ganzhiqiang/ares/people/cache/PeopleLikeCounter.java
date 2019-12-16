package cn.ganzhiqiang.ares.people.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import cn.ganzhiqiang.ares.common.AbstractCounter;
import cn.ganzhiqiang.ares.common.CounterDomain;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * @author nanxuan
 * @since 2018/2/21
 **/

@Repository
public class PeopleLikeCounter extends AbstractCounter {

    private static final String PEOPLE_LIKE_FIELD = "people_like";

    @Resource
    private StringRedisTemplate redisCounter;

    @Override
    protected StringRedisTemplate getCounter() {
        return redisCounter;
    }

    public void incrCount(Integer resourceId, CounterDomain domain) {
        incrCount(String.valueOf(resourceId), String.valueOf(domain.getValue()), PEOPLE_LIKE_FIELD, 1);
    }

    public void decrCount(Integer resourceId, CounterDomain domain) {
        decrCount(String.valueOf(resourceId), String.valueOf(domain.getValue()), PEOPLE_LIKE_FIELD, 1);
    }

    public long findCount(Integer resourceId, CounterDomain domain) {
        return findCount(resourceId, String.valueOf(domain.getValue()), PEOPLE_LIKE_FIELD);
    }

    public Map<Integer, Long> queryCount(List<Integer> resourceIds, CounterDomain domain) {
        return queryCount(resourceIds, String.valueOf(domain.getValue()), PEOPLE_LIKE_FIELD);
    }

}
