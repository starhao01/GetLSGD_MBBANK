package com.viettelpost.core.cfgs;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.viettelpost.core.base.Parser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})

public class DbCoreCfg {

    @Autowired
    Environment env;

    @Bean(name = "coreSource", destroyMethod = "close")
    public DataSource getDataSource() throws Exception {
        String host = env.getRequiredProperty("sys.dbUrl");
        String username = env.getRequiredProperty("sys.dbUsername");
        String password = env.getRequiredProperty("sys.dbPassword");
        int maxPoolSize = Parser.tryParseInt(env.getProperty("sys.dbPoolSize"), 0);
        System.out.println("## getDataSource " + host + "/" + username + "/**" + (password == null ? "" : password.substring(password.length() - 3)));
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
        dataSource.setJdbcUrl(host);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(maxPoolSize);
        dataSource.setMaxIdleTime(64);
        dataSource.setMaxIdleTimeExcessConnections(128);
        dataSource.setUnreturnedConnectionTimeout(10);
        dataSource.setIdleConnectionTestPeriod(128);
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setPreferredTestQuery("SELECT 1 FROM DUAL");
        dataSource.setAcquireRetryDelay(5000);
        dataSource.setAutoCommitOnClose(true);
        dataSource.setAcquireIncrement(1);
        System.out.println("## getDataSource: " + dataSource);
        return dataSource;
    }

    @Autowired
    @Bean(name = "coreFactory")
    public SessionFactory getSessionFactory(@Qualifier("coreSource") DataSource dataSource) throws Exception {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        properties.put("hibernate.show_sql", "false");
        properties.put("current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", false);
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setHibernateProperties(properties);
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();
        SessionFactory sf = factoryBean.getObject();
        System.out.println("## getSessionFactory: " + sf);
        return sf;
    }

    @Autowired
    @Bean(name = "coreTransactionManager")
    public HibernateTransactionManager getTransactionManager(@Qualifier("coreFactory") SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }

    @Bean("oracleEvtpJdbcTemplate")
    public JdbcTemplate createJdbcTemplate(@Autowired @Qualifier("coreSource") DataSource ds) {
        JdbcTemplate template = new JdbcTemplate(ds);
        template.setResultsMapCaseInsensitive(true);
        return template;
    }
}
