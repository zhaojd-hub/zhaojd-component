package org.zhaojd.component.cache.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 默认配置
 * @author jdzhao2
 */
@Configuration
@ConditionalOnProperty(name = "spring.redis.default.enable", havingValue = "true")
@ConfigurationProperties(prefix = "spring.redis.default")
public class DefaultRedisConfig extends AbstractRedisConfig {
    
    @Override
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Serializable> getRedisTemplate(
            @Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return getDefaultRedisTemplate(redisConnectionFactory);
    }
    
    @Override
    @Bean(name = "jsonRedisTemplate")
    public RedisTemplate<String, Object> getJsonRedisTemplate(
            @Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return getDefaultJsonRedisTemplate(redisConnectionFactory);
    }
    
    @Override
    @Bean(name = "stringRedisTemplate")
    public RedisTemplate<String, String> getStringRedisTemplate(
            @Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return getDefaultStringRedisTemplate(redisConnectionFactory);
    }
    
    @Override
    @Bean(name = "defaultRedisConnectionFactory")
    public RedisConnectionFactory getRedisConnectionFactory() {
        return super.getConfigRedisConnectionFactory();
    }
    
    @Override
    @Bean("cacheManager")
    @Primary
    public RedisCacheManager getCacheManager(
            @Qualifier("defaultRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return super.getDefaultCacheManager(redisConnectionFactory);
    }
}