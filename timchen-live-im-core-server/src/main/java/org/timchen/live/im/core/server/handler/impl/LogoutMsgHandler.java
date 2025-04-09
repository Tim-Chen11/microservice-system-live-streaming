package org.timchen.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.apache.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.timchen.live.im.constants.ImMsgCodeEnum;
import org.timchen.live.im.core.server.common.ChannelHandlerContextCache;
import org.timchen.live.im.core.server.common.ImContextUtils;
import org.timchen.live.im.core.server.common.ImMsg;
import org.timchen.live.im.core.server.handler.SimplyHandler;
import org.timchen.live.im.dto.ImMsgBody;

/**
 * @Author: Tim Chen
 * @Date: 15:53 2024-08-04
 * @Description:
 */
@Component

public class LogoutMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        LOGGER.info("[logoutMsg]:"+imMsg);
        byte[] body = imMsg.getBody();
        if(body==null||body.length==0){
            ctx.close();
            LOGGER.error("LogoutMsgHandler [msgHanlder] body error, imMsg is {}", imMsg);
            throw new IllegalArgumentException("body error");
        }
        //有可能为空
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if(userId==null||appId==null){
            LOGGER.error("LogoutMsgHandler [msgHanlder] userId empty, appID empty, imMsg is {}", imMsg);
            throw new IllegalArgumentException("userId 为空， 不做处理");
        }

        ImMsgBody imMsgBodyDTO=new ImMsgBody();
        imMsgBodyDTO.setUserId(userId);
        imMsgBodyDTO.setAppId(appId);
        imMsgBodyDTO.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), JSON.toJSONString(imMsgBodyDTO));
        ctx.writeAndFlush(respMsg);
        LOGGER.info("LogoutMsgHandler [msgHanlder] response imMsg is {}", respMsg);
        //理想情况下，客户端断线的时候，会发送一个断线消息包
        ChannelHandlerContextCache.remove(userId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        ctx.close();
    }
}
