package org.idea.timchen.live.framework.redis.starter.key;

import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: Tim Chen
 * @Date: 14:15 2024-07-09
 * @Description:
 */
public class RedisKeyBuilder {
    @Value("${spring.application.name}")
    private String applicationName;

    private static final String SPLIT_ITEM = ":";

    public String getSplitItem() {
        return SPLIT_ITEM;
    }

    public String getPrefix() {
        return applicationName + SPLIT_ITEM;
    }
}