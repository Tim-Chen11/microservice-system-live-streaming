package imclient;


import imclient.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.timchen.live.im.core.server.common.ImMsg;
import org.timchen.live.im.core.server.common.TcpImMsgDecoder;
import org.timchen.live.im.core.server.common.TcpImMsgEncoder;
import org.timchen.live.im.constants.ImMsgCodeEnum;

/**
 * @Author idea
 * @Date: Created in 11:20 2023/7/1
 * @Description
 */
@SpringBootApplication
public class ImClientApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImClientApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
