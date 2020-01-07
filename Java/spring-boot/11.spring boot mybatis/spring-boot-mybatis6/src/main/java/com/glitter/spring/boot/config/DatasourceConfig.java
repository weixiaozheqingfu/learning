package com.glitter.spring.boot.config;

import com.glitter.spring.boot.context.DatasourceContext;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description
 *
 * @author fxb
 * @date 2018-09-03
 */
@Configuration
@MapperScan(basePackages = "com.example.dxfl.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class DatasourceConfig {

    @Value("${mysql.type-aliases-package}")
    private String typeAliasesPackage;

    @Value("${mysql.mapper-locations}")
    private String mapperLocation;

    @Value("${mysql.config-location}")
    private String configLocation;

    /**
     * 写数据源write
     *
     * @Primary 标志这个Bean如果在多个同类Bean候选时，该Bean优先被考虑。
     * 多数据源配置的时候注意，必须要有一个主数据源，用@Primary标志该Bean
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.write")
    public DataSource writeDataSource() {
        return new HikariDataSource();
    }

    /** 读数据源read0 */
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.read0")
    public DataSource read0() {
        return new HikariDataSource();
    }

    /** 读数据源read1 */
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.read1")
    public DataSource read1() {
        return new HikariDataSource();
    }

    /**
     * 设置数据源路由，通过该类中的determineCurrentLookupKey决定使用哪个数据源
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>(3);
        targetDataSources.put(DatasourceContext.WRITE, writeDataSource());
        targetDataSources.put(DatasourceContext.READ+"0", read0());
        targetDataSources.put(DatasourceContext.READ+"1", read1());

        routingDataSource.setDefaultTargetDataSource(writeDataSource());
        routingDataSource.setTargetDataSources(targetDataSources);

        return routingDataSource;
    }

    /**
     * 多数据源需要自己设置sqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        DataSource dataSource = routingDataSource();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 实体类对应的位置
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        // 设置mybatis的mapper配置文件
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocation));
        // mybatis的config配置文件
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(configLocation));

        SqlSessionFactory  sqlSessionFactory = sqlSessionFactoryBean.getObject();
        return sqlSessionFactory;
    }

    /** 设置事务，事务需要知道当前使用的是哪个数据源才能进行事务处理。事务是aop完成的，只与connection有关，即只与数据源有关 */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSource dataSource = routingDataSource();
        return new DataSourceTransactionManager(dataSource);
    }

}
