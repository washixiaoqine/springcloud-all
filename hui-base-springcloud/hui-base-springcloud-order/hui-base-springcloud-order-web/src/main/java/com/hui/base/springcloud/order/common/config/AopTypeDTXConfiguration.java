package com.hui.base.springcloud.order.common.config;

import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.tc.aspect.interceptor.TxLcnInterceptor;
import com.codingapi.txlcn.tc.aspect.weave.DTXLogicWeaver;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * AOP方式声明本地事务和分布式事务，也可用注解声明事务
 */
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource")
@MapperScan(basePackages = {AopTypeDTXConfiguration.daoPackage}, sqlSessionTemplateRef = "sqlSessionTemplate")
@Data
public class AopTypeDTXConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopTypeDTXConfiguration.class);
    public static final String daoPackage = "com.hui.**.mapper";
    public static final String mapperPackage = "classpath*:/mapper/**/*.xml";

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initialSize;
    private int maxActive;
    private int minIdle;
    private int maxWait;
    private boolean poolPreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private int maxEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private String filters;
    private String connectionProperties;

    /**
     * 本地事务配置
     */
    @Bean("dataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (Exception e) {
            LOGGER.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource db2_dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(db2_dataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperPackage));
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory SqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(SqlSessionFactory);
    }

    /**
     * @Description 声明式事务
     * 1. DataSourceTransactionManager注入事务拦截器
     * 2. 通过定义Properties 自定义拦截方法的事务
     * 3. 返回事务拦截器 TransactionInterceptor
     */
    @Bean(name = "transactionInterceptorCommon")
    public TransactionInterceptor transactionInterceptor(
        @Qualifier("transactionManager")DataSourceTransactionManager dataSourceTransactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(dataSourceTransactionManager);
        Properties transactionAttributes = new Properties();

        transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("remove*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("add*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("create*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("put*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("merge*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("submit*", "PROPAGATION_REQUIRED,-Throwable");

        transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        transactionAttributes.setProperty("select*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        transactionAttributes.setProperty("find*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        transactionAttributes.setProperty("count*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        transactionAttributes.setProperty("list*", "PROPAGATION_REQUIRED,-Throwable,readOnly");

        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
    }

    /**
     * 分布式事务配置 设置为LCN模式
     */
    @ConditionalOnBean(DTXLogicWeaver.class)
    @Bean
    public TxLcnInterceptor txLcnInterceptor(DTXLogicWeaver dtxLogicWeaver) {
        TxLcnInterceptor txLcnInterceptor = new TxLcnInterceptor(dtxLogicWeaver);
        Properties properties = new Properties();
        properties.setProperty(Transactions.DTX_TYPE, Transactions.LCN);
        properties.setProperty(Transactions.DTX_PROPAGATION, "REQUIRED");
        txLcnInterceptor.setTransactionAttributes(properties);
        return txLcnInterceptor;
    }

    /**
     * 通过代理注册事务拦截器Bean
     */
    @Bean(name = "beanNameAutoProxyCreator")
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setInterceptorNames("txLcnInterceptor", "transactionInterceptorCommon");
        beanNameAutoProxyCreator.setBeanNames("*Impl");
        return beanNameAutoProxyCreator;
    }
}
