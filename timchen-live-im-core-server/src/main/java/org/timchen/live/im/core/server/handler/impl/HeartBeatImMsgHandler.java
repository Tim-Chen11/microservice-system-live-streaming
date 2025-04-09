package org.timchen.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.idea.timchen.live.framework.redis.starter.key.ImCoreServerProviderCacheKeyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.timchen.live.im.constants.ImConstants;
import org.timchen.live.im.constants.ImMsgCodeEnum;
import org.timchen.live.im.core.server.common.ImContextUtils;
import org.timchen.live.im.core.server.common.ImMsg;
import org.timchen.live.im.core.server.handler.SimplyHandler;
import org.timchen.live.im.dto.ImMsgBody;
import com.alibaba.fastjson.JSON;


import java.util.concurrent.TimeUnit;

/**
 * @Author: Tim Chen
 * @Date: 15:54 2024-08-04
 * @Description:
 */
@Component
public class HeartBeatImMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatImMsgHandler.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ImCoreServerProviderCacheKeyBuilder imCoreServerProviderCacheKeyBuilder;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        //心跳包基本校验
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            LOGGER.error("HeartBeatMsgHandler [msgHanlder] userId empty, appID empty, imMsg is {}", imMsg);
            throw new IllegalArgumentException("userId 为空， 不做处理");
        }
        //心跳包record记录，redis存储心跳记录

        //zset集合存储心跳记录，基于user Id 去做取模，key(userId)-score(心跳时间)
        String redisKey = imCoreServerProviderCacheKeyBuilder.buildImLoginTokenKey(userId, appId);
        //记录用户最近一次心跳时间到zSet上
        this.recordOnlineTime(userId, redisKey);
        //清理掉过期不在线的用户留下的心跳记录(在两次心跳包的发送间隔中，如果没有重新更新score值，就会导致被删除)
        this.removeExpireRecord(redisKey);
        redisTemplate.expire(redisKey,5, TimeUnit.MINUTES);

        ImMsgBody msgBody = new ImMsgBody();
        msgBody.setUserId(userId);
        msgBody.setAppId(appId);
        msgBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), JSON.toJSONString(msgBody));
        LOGGER.info("[HeartBeatImMsgHandler] imMsg is {}", imMsg);
        ctx.writeAndFlush(respMsg);
    }


    /**
     * 清理掉过期不在线的用户留下的心跳记录(在两次心跳包的发送间隔中，如果没有重新更新score值，就会导致被删除)
     *
     * @param redisKey
     */
    private void removeExpireRecord(String redisKey) {
        redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, System.currentTimeMillis() - ImConstants.DEFAULT_HEART_BEAT_GAP * 1000 * 2);
    }

    /**
     * 记录用户最近一次心跳时间到zSet上
     *
     * @param userId
     * @param redisKey
     */
    private void recordOnlineTime(Long userId, String redisKey) {
        redisTemplate.opsForZSet().add(redisKey, userId, System.currentTimeMillis());
    }
}
