package com.fooddelivery;

import com.food_delivery_app.food_delivery_back_end.FoodDeliveryBackEndApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FoodDeliveryBackEndApplication.class)
public class RedisConnectionTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        redisTemplate.opsForValue().set("test", "Hello Redis");
        Object value = redisTemplate.opsForValue().get("test");
        assertEquals("Hello Redis", value);
    }
}
