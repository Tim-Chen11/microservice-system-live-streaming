package org.timchen.live.web.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Tim Chen
 * @Date: 16:36 2024-08-01
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public TimchenUserInfoInterceptor timchenUserInfoInterceptor(){
        return new TimchenUserInfoInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timchenUserInfoInterceptor()).addPathPatterns("/**").excludePathPatterns("/error");
    }
}