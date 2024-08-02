package org.timchen.live.msg.provider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Tim Chen
 * @Date: 14:48 2024-07-30
 * @Description:
 */
@ConfigurationProperties(prefix = "timchen.sms.ccp")
@Configuration
@Data
public class ApplicationProperties {

    private String smsServerIp;
    private Integer port;
    private String accountSId;
    private String accountToken;
    private String appId;
    private String testPhone;
}
