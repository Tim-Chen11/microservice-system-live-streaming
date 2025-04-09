package org.timchen.live.id.generate.provider;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.timchen.live.id.generate.enums.IdTypeEnum;
import org.timchen.live.id.generate.provider.service.IdGenerateService;

import java.util.HashSet;

/**
 * @Author: Tim Chen
 * @Date: 11:16 2024-07-15
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGenerateApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    /**
     * Testing for id generation
     */
    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public void run(String... args) throws Exception {
//        Long userId = idGenerateService.getUnSeqId(IdTypeEnum.USER_UNSEQ_ID.getCode());
//        System.out.println("dshfkjasdfhjskdfhkjsahfksjadf   "+ userId);
//
//        HashSet<Long> idSet = new HashSet<>();
//        for (int i = 0; i < 1500; i++) {
//            Long id = idGenerateService.getSeqId(1);
//            System.out.println("id: " + id);
//            idSet.add(id);
//        }
//        System.out.println(idSet.size());
    }
}
