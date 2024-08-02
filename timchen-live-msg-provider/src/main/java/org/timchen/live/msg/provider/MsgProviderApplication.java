package org.timchen.live.msg.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.timchen.live.msg.dto.MsgCheckDTO;
import org.timchen.live.msg.enums.MsgSendResultEnum;
import org.timchen.live.msg.provider.dao.mapper.SmsMapper;
import org.timchen.live.msg.provider.dao.po.SmsPO;
import org.timchen.live.msg.provider.service.ISmsService;

import java.util.Scanner;

/**
 * @Author: Tim Chen
 * @Date: 1:37 2024-07-29
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class MsgProviderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(MsgProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Resource
    private ISmsService iSmsService;
    @Resource
    private SmsMapper smsMapper;

    @Override
    public void run(String... args) throws Exception {
//        MsgSendResultEnum msgSendResultEnum = iSmsService.sendLoginCode("15170331193");
//
//        System.out.println("发送结果是： "+msgSendResultEnum);

    }
}
