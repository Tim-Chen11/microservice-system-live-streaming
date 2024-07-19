package org.timchen.live.user.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Tim Chen
 * @Date: 11:03 2024-07-12
 * @Description:
 */

@ConfigurationProperties(prefix="timchen.rmq.producer")
@Configuration
@Data
public class RocketMQProducerProperties {

    //rocketmq nameServer address
    private String nameSrv;
    //group name
    private String groupName;
    // message send failed retry times
    private int retryTimes;
    private int sendTimeOut;
}
