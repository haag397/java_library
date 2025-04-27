package com.library.library.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String ACCESS_BLACKLIST_PREFIX = "token:blacklist:access:";
    private static final String REFRESH_BLACKLIST_PREFIX = "token:blacklist:refresh:";

    public String generateKey(String prefix, String token) {
        return prefix + token;
    }

    // For access tokens (shorter-lived
    public void blackListAccessToken(String token, long ttlSeconds) {
        String key = generateKey(ACCESS_BLACKLIST_PREFIX, token);
        redisTemplate.opsForValue().set(key, "blacklisted", ttlSeconds, TimeUnit.SECONDS);
    }

    public boolean isAccessTokenBlacklisted(String token) {

        String key = generateKey(ACCESS_BLACKLIST_PREFIX, token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // For refresh tokens (longer-lived)
    public void blackListRefreshToken(String token, long ttlSeconds) {
        String key = generateKey(REFRESH_BLACKLIST_PREFIX, token);
        redisTemplate.opsForValue().set(key, "blacklisted", ttlSeconds, TimeUnit.SECONDS);
    }

    public boolean isRefreshTokenBlacklisted(String token) {
        String key = generateKey(REFRESH_BLACKLIST_PREFIX, token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void blackListAllUserToken(String userId, String accessToken, String refreshToken,
                                      long accessTtlSeconds, long refreshTtlSeconds) {
        blackListAccessToken(accessToken, accessTtlSeconds);
        blackListRefreshToken(refreshToken, refreshTtlSeconds);

        String accessUserKey = "user:tokens:access:" + userId + ":" + accessToken;
        String refreshUserKey = "user:tokens:refresh:" + userId + ":" + refreshToken;

        redisTemplate.opsForValue().set(accessUserKey, accessToken, accessTtlSeconds, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(refreshUserKey, refreshToken, refreshTtlSeconds, TimeUnit.SECONDS);

    }

}


