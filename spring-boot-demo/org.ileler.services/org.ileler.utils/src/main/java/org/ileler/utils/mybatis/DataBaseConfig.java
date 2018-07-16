package org.ileler.utils.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Author: kerwin612
 */
@Configuration
@ConditionalOnClass(DataSourceInitializer.class)
//@ConditionalOnResource(resources = "classpath:mybatis-mybatis-mapper/*.xml")
public class DataBaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseConfig.class);

    @Primary
    @Bean(name = "dbDataSource")
    @ConfigurationProperties(prefix = "datasource.db")
    public DataSource dbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbDataSourceInitializer")
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dbDataSource") DataSource dbDataSource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dbDataSource);
        ClassPathResource classPathResource = new ClassPathResource("db.sql");

        if (classPathResource.exists()) {
            LOGGER.info("get db.sql");
            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(classPathResource);
            dataSourceInitializer.setDatabasePopulator(databasePopulator);
            dataSourceInitializer.setEnabled(true);
        } else {
            dataSourceInitializer.setEnabled(false);
        }
        return dataSourceInitializer;
    }

}
