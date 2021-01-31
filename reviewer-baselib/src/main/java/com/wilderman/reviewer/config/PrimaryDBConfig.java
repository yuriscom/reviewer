package com.wilderman.reviewer.config;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManager", transactionManagerRef = "primaryTransactionManager", basePackages = "com.wilderman.reviewer.db.primary.repository")
public class PrimaryDBConfig {
	
	@Autowired
	private JpaProperties properties;
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.primary.datasource")
	public DataSource postgresqlDataSource() {
		return DataSourceBuilder.create().build();
	}
	@Primary
	@Bean(name = "primaryEntityManager")
	public LocalContainerEntityManagerFactoryBean postgresqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(postgresqlDataSource())
				.properties(hibernateProperties())
				.packages("com.wilderman.reviewer.db.primary.entities")
				.persistenceUnit("primaryPU")
				.build();
	}
	@Primary
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager postgresqlTransactionManager(
			@Qualifier("primaryEntityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	private Map<String, Object> hibernateProperties() {
		Map<String, Object> props = properties.getHibernateProperties(new HibernateSettings());
		return props;
	}
}
