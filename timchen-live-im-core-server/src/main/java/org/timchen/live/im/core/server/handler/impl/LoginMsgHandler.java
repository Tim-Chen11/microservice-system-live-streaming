package org.timchen.live.im.core.server.handler.impl;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.timchen.live.im.constants.AppIdEnum;
import org.timchen.live.im.constants.ImMsgCodeEnum;
import org.timchen.live.im.core.server.common.ChannelHandlerContextCache;
import org.timchen.live.im.core.server.common.ImContextAttr;
import org.timchen.live.im.core.server.common.ImContextUtils;
import org.timchen.live.im.core.server.common.ImMsg;
import org.timchen.live.im.core.server.handler.SimplyHandler;
import org.timchen.live.im.dto.ImMsgBody;
import org.timchen.live.im.interfaces.ImTokenRpc;

/**
 * @Author: Tim Chen
 * @Date: 15:51 2024-08-04
 * @Description:
 */
@Component
public class LoginMsgHandler implements SimplyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginMsgHandler.class);

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        //防止重复请求
        if (ImContextUtils.getUserId(ctx) != null) {
            return;
        }
        
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            LOGGER.error("body error,imMsg is {}", imMsg);
            throw new IllegalArgumentException("body error");
        }
        ImMsgBody imMsgBody = JSON.parseObject(new String(body), ImMsgBody.class);
        Long userIdFromMsg = imMsgBody.getUserId();
        Integer appId = imMsgBody.getAppId();
        String token = imMsgBody.getToken();
        if (StringUtils.isEmpty(token) || userIdFromMsg < 10000 || appId < 10000) {
            ctx.close();
            LOGGER.error("param error,imMsg is {}", imMsg);
            throw new IllegalArgumentException("param error");
        }
        Long userId = imTokenRpc.getUserIdByToken(token);
        //token校验成功，而且和传递过来的userId是同一个，则允许建立连接
        if (userId != null && userId.equals(userIdFromMsg)) {
            //按照userId保存好相关的channel对象信息
            ChannelHandlerContextCache.put(userId, ctx);
            ImContextUtils.setUserId(ctx, userId);
            ImContextUtils.setAppId(ctx, appId);
            //将im消息回写给客户端
            ImMsgBody respBody = new ImMsgBody();
            respBody.setAppId(AppIdEnum.TIMCHEN_LIVE_BIZ.getCode());
            respBody.setUserId(userId);
            respBody.setData("true");
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(respBody));
            LOGGER.info("login successfully , userId is {}, appId is {}", userId, appId);
            ctx.writeAndFlush(respMsg);
            return;
        }
        ctx.close();
        LOGGER.error("token check error,imMsg is {}", imMsg);
        throw new IllegalArgumentException("token check error");
    }
}
