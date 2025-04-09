package org.timchen.live.framework.mq.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Tim Chen
 * @Date: 0:25 2024-08-25
 * @Description:
 */
@ConfigurationProperties(prefix = "timchen.rmq.producer")
@Configuration
@Data
public class RocketMQProducerProperties {

    private String nameSrv;
    private String groupName;
    private String applicationName;
    private Integer sendMsgTimeout;
    private Integer retryTimes;}
