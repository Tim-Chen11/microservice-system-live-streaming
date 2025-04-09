package org.timchen.live.im.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.timchen.live.im.constants.AppIdEnum;
import org.timchen.live.im.provider.service.ImTokenService;

/**
 * @Author: Tim Chen
 * @Date: 16:35 2024-08-05
 * @Description:
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class ImProviderApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImProviderApplication.class);
//        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

}
