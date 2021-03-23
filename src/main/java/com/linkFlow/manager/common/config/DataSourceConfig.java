package com.linkFlow.manager.common.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig
{
    private static final String MYBATIS_CONFIG_FILE_PATH = "mybatis-config.xml";

    @Autowired
    private ConfigDataManager configDataManager;

    @Bean
    public BasicDataSource dataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();

        configDataManager.changeDataSource(dataSource);

        dataSource.setValidationQuery("select 1");
        dataSource.setTestOnBorrow(true);

        dataSource.setInitialSize(10);
        dataSource.setMaxIdle(50);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(2048);
        dataSource.setMaxWait(1000);

        return dataSource;
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG_FILE_PATH));
        sqlSessionFactoryBean.setTypeAliasesPackage("com/linkFlow/manager/common/model");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/linkFlow/manager/common/dao/mybatis/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSession sqlSession() throws Exception
    {
        return new SqlSessionTemplate(this.sqlSessionFactory());
    }

    @Bean(name = "apiTransactionManager")
    public PlatformTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }
}