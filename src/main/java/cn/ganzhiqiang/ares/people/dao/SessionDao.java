package cn.ganzhiqiang.ares.people.dao;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

/**
 * @author nanxuan
 * @since 2018/1/21
 **/

@Repository
public class SessionDao {

    @Resource
    private StringRedisTemplate redisSession;

    public boolean saveSession(String key, String value, long ttl) {
        redisSession.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
        return true;
    }

}
