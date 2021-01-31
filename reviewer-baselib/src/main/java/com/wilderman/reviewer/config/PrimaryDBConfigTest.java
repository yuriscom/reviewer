package com.wilderman.reviewer.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManager", transactionManagerRef = "primaryTransactionManager")
//@PropertySource(value = "classpath:app.properties")
public class PrimaryDBConfigTest {
//    private static final Logger log = LoggerFactory.getLogger(PrimaryDBConfigTest.class);
//
//    @Autowired
//    private JpaProperties jpaProperties;
//
//
//    @Autowired
//    private HibernateProperties hibernateProperties;
//
//    @Value("${spring.primary.datasource.hikari.maximumPoolSize:10}")
//    protected int poolSize;
//
//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.primary.datasource")
//    public DataSource postgresqlDataSource() {
//        log.info("START1");
//        DataSource dataSource = DataSourceBuilder.create().build();
//        HikariDataSource newds = (HikariDataSource) dataSource;
//        newds.setMaximumPoolSize(poolSize);
//        log.info("DATASOURCE = " + newds.getMaximumPoolSize());
//        log.info("END1");
//        return dataSource;
//    }
//
//    @Primary
//    @Bean(name = "primaryEntityManager")
//    public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
//        log.info("START2");
//        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = builder.dataSource(postgresqlDataSource()).properties(hibernateProperties())
//                .packages("com.stagwelltech.nrg.entities.nrg").persistenceUnit("primaryPU").build();
//        log.info("END2");
//        return localContainerEntityManagerFactoryBean;
//    }
//
//    @Primary
//    @Bean(name = "primaryTransactionManager")
//    public PlatformTransactionManager postgresqlTransactionManager(
//            @Qualifier("primaryEntityManager") EntityManagerFactory entityManagerFactory) {
//        log.info("START3");
//        PlatformTransactionManager platformTransactionManager = new JpaTransactionManager(entityManagerFactory);
//        log.info("END3");
//        return platformTransactionManager;
//    }
//
//    @Primary
//    private Map<String, Object> hibernateProperties() {
//        log.info("START4");
//        Map<String, Object> hb = hibernateProperties.determineHibernateProperties(
//                jpaProperties.getProperties(), new HibernateSettings());
//        log.info("END4");
//        return hb;
//    }
//
//    @Primary
//    @Bean(name = "jdbcTemplatePostgresql")
//    public JdbcTemplate jdbcTemplate() {
//        log.info("START5");
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(postgresqlDataSource());
//        jdbcTemplate.setResultsMapCaseInsensitive(true);
//        log.info("END5");
//        return jdbcTemplate;
//    }
//
//
//    @PostConstruct
//    public void postconstruct() {
//        log.info("LOADED");
//    }
}