package org.ileler.utils.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;


/**
 * Author: kerwin612
 */
@Configuration
@ConditionalOnClass(DataSourceInitializer.class)
//@ConditionalOnResource(resources = "classpath:mybatis-mybatis-mapper/*.xml")
public class ServiceDBConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDBConfiguration.class);

    @Bean(name = "sdbDataSource")
    @ConfigurationProperties(prefix = "datasource.sdb")
    public DataSource dbDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sdbSqlSessionFactory")
    public SqlSessionFactory sdbSqlSessionFactory(@Qualifier("sdbDataSource") DataSource sdbDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(sdbDataSource);
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-mapper/*.xml");
            if (resources != null) {
                bean.setMapperLocations(resources);
                LOGGER.info("set mybatis-mapper resources:{}", resources);
            }
        } catch (Exception e) {}
        return bean.getObject();
    }

    @Bean(name = "sdbTransactionManager")
    public DataSourceTransactionManager sdbTransactionManager(@Qualifier("sdbDataSource") DataSource sdbDataSource) {
        return new DataSourceTransactionManager(sdbDataSource);
    }

    @Bean(name = "sdbSqlSessionTemplate")
    public SqlSessionTemplate sdbSqlSessionTemplate(
            @Qualifier("sdbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sdbDataSourceInitializer")
    public DataSourceInitializer dataSourceInitializer(@Qualifier("sdbDataSource") DataSource sdbDataSource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(sdbDataSource);
        ClassPathResource classPathResource = new ClassPathResource("table.sql");

        if (classPathResource.exists()) {
            LOGGER.info("get table.sql");
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
