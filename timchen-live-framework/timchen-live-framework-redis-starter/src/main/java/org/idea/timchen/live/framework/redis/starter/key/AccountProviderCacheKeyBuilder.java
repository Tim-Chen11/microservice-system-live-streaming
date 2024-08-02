package org.idea.timchen.live.framework.redis.starter.key;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Tim Chen
 * @Date: 17:01 2024-07-30
 * @Description:
 */
@Configuration
@Conditional(RedisKeyLoadMatch.class)
public class AccountProviderCacheKeyBuilder extends RedisKeyBuilder {

    private static String ACCOUNT_TOKEN_KEY = "account";

    public String buildUserLoginTokenKey(String key) {
        return super.getPrefix() + ACCOUNT_TOKEN_KEY + super.getSplitItem() + key;
    }
}
