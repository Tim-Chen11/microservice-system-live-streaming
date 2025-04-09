package org.timchen.live.im.core.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.timchen.live.im.core.server.common.ChannelHandlerContextCache;
import org.timchen.live.im.core.server.common.ImContextUtils;
import org.timchen.live.im.core.server.common.ImMsg;

/**
 * @Author: Tim Chen
 * @Date: 15:25 2024-08-04
 * @Description:
 */
@Component
@ChannelHandler.Sharable
public class TcpImServerCoreHandler extends SimpleChannelInboundHandler {

    @Resource
    private ImHandlerFactory imHandlerFactory;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ImMsg)) {
            throw new IllegalArgumentException("error msg,msg is :" + msg);
        }
        ImMsg imMsg = (ImMsg) msg;
        imHandlerFactory.doMsgHandler(ctx, imMsg);
    }

    /**
     * 正常或者意外断线，都会触发到这里
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId != null && appId != null) {
            System.out.println("当前的channel已去除链接");
            ChannelHandlerContextCache.remove(userId);
        }
//        Long userId = ImContextUtils.getUserId(ctx);
//        Integer appId = ImContextUtils.getAppId(ctx);
//        if (userId != null && appId != null) {
//            logoutMsgHandler.logoutHandler(ctx,userId,appId);
//        }
    }
}
