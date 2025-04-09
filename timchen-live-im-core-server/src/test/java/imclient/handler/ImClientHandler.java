package imclient.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.dubbo.config.annotation.DubboReference;
//import org.timchen.live.im.constants.AppIdEnum;
//import org.timchen.live.im.constants.ImMsgCodeEnum;
import org.timchen.live.im.constants.AppIdEnum;
import org.timchen.live.im.constants.ImMsgCodeEnum;
import org.timchen.live.im.core.server.common.ImMsg;
import org.timchen.live.im.core.server.common.TcpImMsgDecoder;
import org.timchen.live.im.core.server.common.TcpImMsgEncoder;
//import org.timchen.live.im.dto.ImMsgBody;
//import org.timchen.live.im.interfaces.ImTokenRpc;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.timchen.live.im.dto.ImMsgBody;
import org.timchen.live.im.interfaces.ImTokenRpc;

import java.util.*;

/**
 * @Author idea
 * @Date: Created in 21:53 2023/7/9
 * @Description
 */
@Service
public class ImClientHandler implements InitializingBean {

    @DubboReference
    private ImTokenRpc imTokenRpc;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup clientGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(clientGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("初始化连接建立");
                        ch.pipeline().addLast(new TcpImMsgDecoder());
                        ch.pipeline().addLast(new TcpImMsgEncoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
                Map<Long, Channel> userIdChannelMap = new HashMap<>();
                for (int i = 0; i < 10; i++) {
                    Long userId = 100000L + i;
                    String token = imTokenRpc.createImLoginToken(userId, AppIdEnum.TIMCHEN_LIVE_BIZ.getCode());
                    ChannelFuture channelFuture = null;
                    try {
                        channelFuture = bootstrap.connect("localhost", 8085).sync();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Channel channel = channelFuture.channel();
                    ImMsgBody imMsgBody = new ImMsgBody();
                    imMsgBody.setAppId(AppIdEnum.TIMCHEN_LIVE_BIZ.getCode());
                    imMsgBody.setToken(token);
                    imMsgBody.setUserId(userId);
                    ImMsg loginMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(imMsgBody));
                    channel.writeAndFlush(loginMsg);
                    userIdChannelMap.put(userId, channel);
                }

                while (true) {
                    for (Long userId : userIdChannelMap.keySet()) {
                        ImMsgBody bizBody = new ImMsgBody();
                        bizBody.setAppId(AppIdEnum.TIMCHEN_LIVE_BIZ.getCode());
                        bizBody.setUserId(userId);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userId", userId);
                        jsonObject.put("objectId", 1001101L);
                        jsonObject.put("content", "你好,我是" + userId);
                        bizBody.setData(JSON.toJSONString(jsonObject));
                        ImMsg heartBeatMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), JSON.toJSONString(bizBody));
                        userIdChannelMap.get(userId).writeAndFlush(heartBeatMsg);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        clientThread.start();
    }

}
