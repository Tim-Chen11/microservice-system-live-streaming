package org.timchen.live.im.core.server;


import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Tim Chen
 * @Date: 19:04 2024-08-03
 * @Description:
 */
@Data
@SpringBootApplication
public class ImCoreServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImCoreServerApplication.class);
//        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
