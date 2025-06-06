package com.henriquecipriano.spring6_read_replica_postgresql.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
class DataSourceConfig {

    private static final String PRIMARY_DATABASE_PROPERTY_KEY_PREFIX = "spring.primary.datasource";
    private static final String REPLICA_DATABASE_PROPERTY_KEY_PREFIX = "spring.replica.datasource";

    @Bean
    @ConfigurationProperties(PRIMARY_DATABASE_PROPERTY_KEY_PREFIX)
    DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(PRIMARY_DATABASE_PROPERTY_KEY_PREFIX + ".hikari")
    DataSource primaryDataSource(final DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @ConfigurationProperties(REPLICA_DATABASE_PROPERTY_KEY_PREFIX)
    DataSourceProperties replicaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(REPLICA_DATABASE_PROPERTY_KEY_PREFIX + ".hikari")
    DataSource replicaDataSource(final DataSourceProperties replicaDataSourceProperties) {
        return replicaDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    DataSource dataSource(final DataSource primaryDataSource, final DataSource replicaDataSource) {
        var ds = new LazyConnectionDataSourceProxy(primaryDataSource);
        ds.setReadOnlyDataSource(replicaDataSource);
        ds.setDefaultAutoCommit(false);
        return ds;
    }
}
