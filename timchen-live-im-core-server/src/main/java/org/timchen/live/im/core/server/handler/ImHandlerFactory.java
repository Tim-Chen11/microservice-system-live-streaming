package org.timchen.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import org.timchen.live.im.core.server.common.ImMsg;

/**
 * @Author: Tim Chen
 * @Date: 15:55 2024-08-04
 * @Description:
 */
public interface ImHandlerFactory {
    /**
     * 按照immsg的code去筛选
     *
     * @param channelHandlerContext
     * @param imMsg
     */
    void doMsgHandler(ChannelHandlerContext channelHandlerContext, ImMsg imMsg);
}
