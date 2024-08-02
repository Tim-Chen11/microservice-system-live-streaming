package org.timchen.live.account.provider.service.impl;

import jakarta.annotation.Resource;
import org.idea.timchen.live.framework.redis.starter.key.AccountProviderCacheKeyBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.timchen.live.account.provider.service.IAccountTokenService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Tim Chen
 * @Date: 16:50 2024-07-30
 * @Description:
 */
@Service
public class AccountTokenServiceImpl implements IAccountTokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private AccountProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(cacheKeyBuilder.buildUserLoginTokenKey(token), String.valueOf(userId), 30, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        String redisKey = cacheKeyBuilder.buildUserLoginTokenKey(tokenKey);
        Integer userId = (Integer) redisTemplate.opsForValue().get(redisKey);
        return userId == null ? null : Long.valueOf(userId);
    }
}
