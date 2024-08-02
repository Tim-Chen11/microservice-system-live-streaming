package org.timchen.live.account.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.timchen.live.account.provider.service.IAccountTokenService;

/**
 * @Author: Tim Chen
 * @Date: 16:00 2024-07-30
 * @Description:
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class AccountProviderApplication implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(AccountProviderApplication.class);
//        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);

    }



    @Resource
    private IAccountTokenService accountTokenService;

    @Override
    public void run(String... args) throws Exception {
//        Long userId = 1092813L;
//        String token = accountTokenService.createAndSaveLoginToken(userId);
//        System.out.println("token is : " + token);
//        Long matchUserId = accountTokenService.getUserIdByToken(token);
//        System.out.println("matchUserId is : " + matchUserId);

    }
}
