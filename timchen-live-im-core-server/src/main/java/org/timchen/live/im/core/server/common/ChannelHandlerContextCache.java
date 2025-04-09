package org.timchen.live.im.core.server.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Tim Chen
 * @Date: 10:06 2024-08-06
 * @Description:
 */
public class ChannelHandlerContextCache {

    private static Map<Long, ChannelHandlerContext> channelHandlerContextMap = new HashMap<>();

    public static ChannelHandlerContext get(Long userId) {
        return channelHandlerContextMap.get(userId);
    }

    public static void put(Long userId, ChannelHandlerContext channelHandlerContext) {
        channelHandlerContextMap.put(userId, channelHandlerContext);
    }

    public static void remove(Long userId) {
        channelHandlerContextMap.remove(userId);
    }

}
