package org.timchen.live.user.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.timchen.live.user.constants.UserTagsEnum;
import org.timchen.live.user.dto.UserDTO;
import org.timchen.live.user.dto.UserLoginDTO;
import org.timchen.live.user.provider.service.IUserPhoneService;
import org.timchen.live.user.provider.service.IUserService;
import org.timchen.live.user.provider.service.IUserTagService;


import java.util.concurrent.CountDownLatch;

/**
 * @Author: Tim Chen
 * @Date: 9:55 2024-06-03
 * @Description: User Middle Platform Provider
 */
@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class UserProviderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Resource
    private IUserTagService userTagService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProviderApplication.class);

    @Resource
    private IUserService userService;

    @Resource
    private IUserPhoneService userPhoneService;

    @Override
    public void run(String... args) throws Exception {
//        String phone = "17789829049";
//        UserLoginDTO userLoginDTO = userPhoneService.login(phone);
//        System.out.println(userLoginDTO);
//        System.out.println(userPhoneService.queryByUserId(userLoginDTO.getUserId()));
//        System.out.println(userPhoneService.queryByUserId(userLoginDTO.getUserId()));
//        System.out.println(userPhoneService.queryByPhone(phone));
//        System.out.println(userPhoneService.queryByPhone(phone));

    }
}
