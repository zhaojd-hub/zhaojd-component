package org.zhaojd.component.cache.config;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author jdzhao2
 */
public abstract class AbstractRedisConfig extends RedisProperties {
    
    private boolean testOnBorrow = true;
    private boolean testOnReturn = true;
    private boolean testWhileIdle = true;
    
    private long connectionTimeoutInMillis = 2000;
    private long readTimeoutInMillis = 2000;
    
    private Map<String, Map<String, String>> cache;
    
    
    public Map<String, Map<String, String>> getCache() {
        return cache;
    }
    
    public void setCache(Map<String, Map<String, String>> cache) {
        this.cache = cache;
    }
    
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }
    
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    public boolean isTestOnReturn() {
        return testOnReturn;
    }
    
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }
    
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    public long getConnectionTimeoutInMillis() {
        return connectionTimeoutInMillis;
    }
    
    public void setConnectionTimeoutInMillis(long connectionTimeoutInMillis) {
        this.connectionTimeoutInMillis = connectionTimeoutInMillis;
    }
    
    public long getReadTimeoutInMillis() {
        return readTimeoutInMillis;
    }
    
    public void setReadTimeoutInMillis(long readTimeoutInMillis) {
        this.readTimeoutInMillis = readTimeoutInMillis;
    }
    
    public abstract RedisConnectionFactory getRedisConnectionFactory();
    
    public abstract RedisTemplate<String, Serializable> getRedisTemplate(RedisConnectionFactory redisConnectionFactory);
    
    public abstract RedisTemplate<String, String> getStringRedisTemplate(RedisConnectionFactory redisConnectionFactory);
    
    public abstract RedisTemplate<String, Object> getJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory);
    
    public abstract RedisCacheManager getCacheManager(RedisConnectionFactory redisConnectionFactory);
    
    public RedisConnectionFactory getConfigRedisConnectionFactory() {
        JedisPoolConfig poolConfig = getJedisPoolConfig(getJedis().getPool());
        
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisBuilder =
                JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig);
        
        jedisBuilder.and().connectTimeout(Duration.ofMillis(connectionTimeoutInMillis));
        jedisBuilder.and().readTimeout(Duration.ofMillis(readTimeoutInMillis));
        
        JedisClientConfiguration jedisClientConfiguration = jedisBuilder.build();
        
        // ????????????????????????
        if (null != getCluster() && getCluster().getNodes().size() > 0) {
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(getCluster().getNodes());
            if (null != getCluster().getMaxRedirects()) {
                clusterConfig.setMaxRedirects(getCluster().getMaxRedirects());
            }
            
            return new JedisConnectionFactory(clusterConfig, jedisClientConfiguration);
        } else {
            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
            standaloneConfig.setDatabase(getDatabase());
            standaloneConfig.setHostName(getHost());
            standaloneConfig.setPort(getPort());
            standaloneConfig.setPassword(getPassword());
            return new JedisConnectionFactory(standaloneConfig, jedisClientConfiguration);
        }
    }
    
    private JedisPoolConfig getJedisPoolConfig(Pool pool) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        config.setTestOnBorrow(testOnBorrow);
        config.setTestWhileIdle(testWhileIdle);
        config.setTestOnReturn(testOnReturn);
        if (pool.getMaxWait() != null) {
            config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        return config;
    }
    
    public RedisTemplate<String, Serializable> getDefaultRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key??????????????????
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value??????????????????
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
    
    public RedisTemplate<Serializable, Serializable> getDefaultSerializableRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key??????????????????
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        // value??????????????????
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
    
    public RedisTemplate<String, String> getDefaultStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> stringRedisTemplate = new RedisTemplate<>();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        // key??????????????????
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        // value??????????????????
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }
    
    public RedisTemplate<String, Object> getDefaultJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key??????????????????
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // ??????Jackson2JsonRedisSerialize ?????????????????????(??????????????????JDK?????????)
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // value ???????????????
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }
    
    public RedisCacheManager getDefaultCacheManager(RedisConnectionFactory redisConnectionFactory) {
        // ?????????????????????????????????config??????????????????????????????????????????
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        
        // ?????????????????????????????????10?????????????????????Duration??????
        config = config.entryTtl(Duration.ofMinutes(10));
        
        Map<String, Map<String, String>> cacheConfig = getCache();
        Set<String> cacheNames = new HashSet<>(cacheConfig.keySet());
        
        // ??????????????????????????????????????????
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>(16);
        for (String cacheName : cacheNames) {
            configMap.put(cacheName,
                    config.entryTtl(Duration.ofSeconds(Long.parseLong(cacheConfig.get(cacheName).get("ttl")))));
        }
        return RedisCacheManager
                // ?????????????????????????????????????????????cacheManager
                .builder(redisConnectionFactory)
                // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .cacheDefaults(config)
                .build();
    }
}