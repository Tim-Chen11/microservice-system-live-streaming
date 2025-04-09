package org.timchen.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.timchen.live.im.core.server.common.ImMsg;

/**
 * @Author: Tim Chen
 * @Date: 15:49 2024-08-04
 * @Description:
 */
public interface SimplyHandler {

    /**
     * 消息处理函数
     *
     * @param ctx
     * @param imMsg
     */
    void handler(ChannelHandlerContext ctx, ImMsg imMsg);
}