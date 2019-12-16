package cn.ganzhiqiang.ares.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

import cn.ganzhiqiang.ares.common.helper.GConfigHelper;

import javax.annotation.Resource;

/**
 * @author nanxuan
 * @since 2017/12/19
 **/

@Configuration
public class CacheConfig {

    @Resource
    private GConfigHelper configHelper;

    @Bean(name = {"redisPersist", "redisCache", "redisCounter", "redisSession"})
    public StringRedisTemplate redisTemplate() {
        // redis连接池的两个配置参数testWhileIdle、testOnBorrow为True，分别表示 在空闲时检查有效性、
        // 在获取连接的时候检查有效性 检查到无效连接时，会清理掉无效的连接，并重新获取新的连接。
        String redisHost = configHelper.getValueByKey("redis.cache.host", String.class);
        Integer redisPort = configHelper.getValueByKey("redis.cache.port", Integer.class);
        String password = configHelper.getValueByKey("redis.cache.password", String.class);

        JedisPoolConfig config = createConfig(20, 10, 3000, false, false);
        JedisConnectionFactory factory = createFactory(redisHost, redisPort, password, 0, true, config);
        return new StringRedisTemplate(factory);
    }

    public JedisPoolConfig createConfig(int maxTotal, int maxIdle, long maxWaitMillis, boolean testOnBorrow, boolean testWhileIdle) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis((long) maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestWhileIdle(testWhileIdle);
        return config;
    }

    public JedisConnectionFactory createFactory(String host, int port, String password, int db, boolean usePool, JedisPoolConfig config) {
        JedisConnectionFactory fact = new JedisConnectionFactory(config);
        fact.setUsePool(usePool);
        fact.setPassword(password);
        fact.setHostName(host);
        fact.setPort(port);
        fact.setDatabase(db);
        fact.afterPropertiesSet();
        return fact;
    }

}
