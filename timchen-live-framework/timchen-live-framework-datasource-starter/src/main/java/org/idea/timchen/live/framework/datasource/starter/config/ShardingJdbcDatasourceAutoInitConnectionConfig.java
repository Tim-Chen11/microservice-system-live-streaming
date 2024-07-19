package org.idea.timchen.live.framework.datasource.starter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author: Tim Chen
 * @Date: 14:28 2024-07-08
 * @Description:
 */
@Configuration
public class ShardingJdbcDatasourceAutoInitConnectionConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(ShardingJdbcDatasourceAutoInitConnectionConfig.class);

    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        return args -> {
            LOGGER.info(" ================== [ShardingJdbcDatasourceAutoInitConnectionConfig] dataSource: {}", dataSource);
            Connection connection = dataSource.getConnection();
        };
    }
}
