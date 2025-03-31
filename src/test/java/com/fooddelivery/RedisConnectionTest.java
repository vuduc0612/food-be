package com.fooddelivery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisConnection() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("test", "Hello Redis");
        String value = ops.get("test");
        assertEquals("Hello Redis", value);
    }
    //C:\Program Files\Common Files\Oracle\Java\javapath
    //C:\ProgramData\Oracle\Java\javapath
} 