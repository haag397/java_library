package com.library.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RedisTestController {

    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test-redis")
    @PreAuthorize("hasRole('ADMIN')")
    public String testRedis() {
        // Test SET/GET operations
        String key = "test:key";
        String value = "test-value";

        // Write to Redis
        redisTemplate.opsForValue().set(key, value, 60, TimeUnit.SECONDS);

        // Read from Redis

        return redisTemplate.opsForValue().get(key);
    }
}
