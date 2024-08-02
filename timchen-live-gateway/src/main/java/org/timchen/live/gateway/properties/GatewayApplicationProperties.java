package org.timchen.live.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: Tim Chen
 * @Date: 21:12 2024-07-31
 * @Description:
 */
@ConfigurationProperties(prefix = "timchen.gateway")
@Configuration
@RefreshScope
@Data
public class GatewayApplicationProperties {

    private List<String> notCheckUrlList;

}

