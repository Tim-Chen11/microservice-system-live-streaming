package org.timchen.live.im.core.server.handler.impl;

import io.netty.channel.ChannelHandlerContext;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.timchen.live.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.timchen.live.im.core.server.common.ImContextUtils;
import org.timchen.live.im.core.server.common.ImMsg;
import org.timchen.live.im.core.server.handler.SimplyHandler;

/**
 * @Author: Tim Chen
 * @Date: 15:53 2024-08-04
 * @Description:
 */
@Component
public class BizImMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BizImMsgHandler.class);


    @Resource
    private MQProducer mqProducer;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        //前期的参数校验
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            LOGGER.error("attr error,imMsg is {}", imMsg);
            //有可能是错误的消息包导致，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            LOGGER.error("body error,imMsg is {}", imMsg);
            return;
        }
        Message message = new Message();
        message.setTopic(ImCoreServerProviderTopicNames.TIMCHEN_LIVE_IM_BIZ_MSG_TOPIC);
        message.setBody(body);
        try {
            SendResult sendResult = mqProducer.send(message);
            LOGGER.info("[BizImMsgHandler]消息投递结果:{}", sendResult);
        } catch (Exception e) {
            LOGGER.error("send error , erros is :", e);
            throw new RuntimeException(e);
        }
    }
}
