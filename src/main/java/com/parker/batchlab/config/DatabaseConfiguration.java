package com.parker.batchlab.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Value("${mainDatasource.driver}") private String mainDatasourceDriver;
    @Value("${mainDatasource.url}") private String mainDatasourceUrl;
    @Value("${mainDatasource.username}") private String mainDatasourceUsername;
    @Value("${mainDatasource.password}") private String mainDatasourcePassword;

    @Bean
    @BatchDataSource
    public DataSource mainDatasource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(mainDatasourceDriver);
        config.setJdbcUrl(mainDatasourceUrl);
        config.setUsername(mainDatasourceUsername);
        config.setPassword(mainDatasourcePassword);
        return new HikariDataSource(config);
    }
}
