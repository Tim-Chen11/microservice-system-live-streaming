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

    @Override
    public void run(String... args) throws Exception {
//        Long userId = 1004L;
//        UserDTO userDTO = userService.getByUserId(userId);
//        userDTO.setNickName("test-nick-name");
//        userService.updateUserInfo(userDTO);
//
//        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println(userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));



//        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_RICH));
//        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_RICH));

//        System.out.println("is_rich:" + userTagService.containTag(userId, UserTagsEnum.IS_RICH));
//        Thread.sleep(1000);
//        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_VIP));
//        System.out.println("is_vip:" + userTagService.containTag(userId, UserTagsEnum.IS_VIP));
//        Thread.sleep(1000);
//        System.out.println(userTagService.setTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println("is_old:" + userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println("------------------------------------------------");
//        Thread.sleep(1000);
//
//        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_RICH));
//        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_RICH));
//        System.out.println("is_rich:" + userTagService.containTag(userId, UserTagsEnum.IS_RICH));
//        Thread.sleep(1000);
//        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_VIP));
//        System.out.println("is_vip:" + userTagService.containTag(userId, UserTagsEnum.IS_VIP));
//        Thread.sleep(1000);
//        System.out.println(userTagService.cancelTag(userId, UserTagsEnum.IS_OLD_USER));
//        System.out.println("is_old:" + userTagService.containTag(userId, UserTagsEnum.IS_OLD_USER));

    }
}
