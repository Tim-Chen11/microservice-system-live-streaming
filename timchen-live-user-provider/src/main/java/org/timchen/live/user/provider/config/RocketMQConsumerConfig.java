package org.timchen.live.user.provider.config;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.idea.timchen.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.timchen.live.user.constants.CacheAsyncDeleteCode;
import org.timchen.live.user.constants.UserProviderTopicNames;
import org.timchen.live.user.dto.UserCacheAsyncDeleteDTO;
import org.timchen.live.user.dto.UserDTO;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Tim Chen
 * @Date: 14:00 2024-07-12
 * @Description:
 */
@Configuration

public class RocketMQConsumerConfig implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQConsumerConfig.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        initConsumer();
    }

    public void initConsumer() {
        try {
            DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
            defaultMQPushConsumer.setVipChannelEnabled(false);
            defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
            defaultMQPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + RocketMQConsumerConfig.class.getSimpleName());
            defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            defaultMQPushConsumer.subscribe(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC, "");
            defaultMQPushConsumer.setMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    String json = new String(msgs.get(0).getBody());
                    UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = JSON.parseObject(json, UserCacheAsyncDeleteDTO.class);

                    if (userCacheAsyncDeleteDTO == null) {
                        LOGGER.error("缓存删除对象为空，参数异常，内容：{}", json);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    Long userId = JSON.parseObject(userCacheAsyncDeleteDTO.getJson()).getLong("userId");
                    if (userId == null) {
                        LOGGER.error("用户id为空，参数异常，内容{}", json);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    if (CacheAsyncDeleteCode.USER_INFO_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                        redisTemplate.delete(cacheKeyBuilder.buildUserInfoKey(userId));
                        LOGGER.info("延迟双删用户信息缓存, userId is {}", userId);
                    } else if (CacheAsyncDeleteCode.USER_TAG_DELETE.getCode() == userCacheAsyncDeleteDTO.getCode()) {
                        redisTemplate.delete(cacheKeyBuilder.buildTagInfoKey(userId));
                        LOGGER.info("延迟双删用户标签缓存, userId is {}", userId);
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
            LOGGER.info("mq消费者启动成功, nameSrv is {}", rocketMQConsumerProperties.getNameSrv());
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }

    }
}
