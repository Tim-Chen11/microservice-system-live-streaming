package org.timchen.live.framework.mq.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Tim Chen
 * @Date: 0:24 2024-08-25
 * @Description:
 */
@ConfigurationProperties(prefix = "timchen.rmq.consumer")
@Configuration
@Data
public class RocketMQConsumerProperties {

    private String nameSrv;
    private String groupName;
}
