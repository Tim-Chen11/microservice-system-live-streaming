package org.timchen.live.user.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Tim Chen
 * @Date: 13:56 2024-07-12
 * @Description:
 */
@ConfigurationProperties(prefix = "timchen.rmq.consumer")
@Configuration
@Data
public class RocketMQConsumerProperties {
    //rocketmq nameServer address
    private String nameSrv;
    //group name
    private String groupName;
}
