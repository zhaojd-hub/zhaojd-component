package org.zhaojd.component.cahce.single;

import java.io.Serializable;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;
import org.zhaojd.component.cahce.dto.BizObj;
import org.zhaojd.component.cahce.single.service.BizService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSingleInstanceTest {
    
    @Autowired
    @Qualifier("stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;
    
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Serializable> redisTemplate;
    
    @Autowired
    @Qualifier("jsonRedisTemplate")
    private RedisTemplate<String, Object> jsonRedisTemplate;
    
    @Autowired
    private BizService bizService;
    
    @Test
    public void testStringRedisTemplate() {
        for (int i = 0; i < 10000000; i++) {
            stringRedisTemplate.boundValueOps("test_single_redis_string_key").set("test_single_redis_string_value");
            String value = stringRedisTemplate.boundValueOps("test_single_redis_string_key").get();
            System.out.println("单实例redis测试，从redis中取出值为：" + value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void testRedisTemplate() {
        BizObj bizObj = new BizObj();
        bizObj.setId(200);
        bizObj.setBizName("测试返回消息内容");
        redisTemplate.opsForValue().set("test_single_redis_key", bizObj);
        BizObj ret = (BizObj) redisTemplate.opsForValue().get("test_single_redis_key");
        assert ret != null;
        System.out.println("从redis中取出内容为：id = " + ret.getId() + " ,BizName=" + ret.getBizName());
    }
    
    @Test
    public void testJsonRedisTemplate() {
        BizObj bizObj = new BizObj();
        bizObj.setId(200);
        bizObj.setBizName("测试返回消息内容");
        bizObj.setDesc("test_desc内容");
        jsonRedisTemplate.opsForValue().set("test_json_redis_key", bizObj);
        BizObj ret = (BizObj) jsonRedisTemplate.opsForValue().get("test_json_redis_key");
        assert ret != null;
        System.out.println("从redis中取出内容为：id = " + ret.getId() + " ,bizName=" + ret.getBizName());
    }
    
    @Test
    public void testRedisCacheAble() {
        System.out.println("第1次查询");
        bizService.findObjById(100);
        System.out.println("第2次查询");
        bizService.findObjById(100);
        System.out.println("第3次查询");
        bizService.findObjById(100);
    }
    
    // 对比redis使用connection和直接使用redisTemplate的性能差别(使用锁的操作流程)
    @Test
    public void testRedisPerformance() {
        testTemplatePer();
        testConnectionPer();
        testTemplatePer();
        testConnectionPer();
        testTemplatePer();
        testConnectionPer();
        testTemplatePer();
    }
    
    private void testTemplatePer() {
        long templateStart = System.currentTimeMillis();
        System.out.println("使用template开始：" + templateStart);
        for (int i = 0; i < 1000; i++) {
            templateRedis("test_key");
        }
        long templateEnd = System.currentTimeMillis();
        System.out.println("使用template结束：" + templateEnd + "，共耗时：" + (templateEnd - templateStart));
    }
    
    private long testConnectionPer() {
        long connectionStart = System.currentTimeMillis();
        System.out.println("使用connection开始：" + connectionStart);
        for (int i = 0; i < 1000; i++) {
            connectionRedis("test_key");
        }
        long connectionEnd = System.currentTimeMillis();
        System.out.println("使用connection结束：" + connectionStart + "，共耗时：" + (connectionEnd - connectionStart));
        return connectionStart;
    }
    
    public void connectionRedis(final String key) {
        Object obj = null;
        try {
            obj = stringRedisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = connection.get(Objects.requireNonNull(serializer.serialize(key)));
                    connection.close();
                    if (data == null) {
                        return null;
                    }
                    return serializer.deserialize(data);
                }
            });
        } catch (Exception e) {
            System.out.println("get redis error, key : " + key);
        }
    }
    
    public void templateRedis(final String key) {
        stringRedisTemplate.boundValueOps(key).get();
    }
}
