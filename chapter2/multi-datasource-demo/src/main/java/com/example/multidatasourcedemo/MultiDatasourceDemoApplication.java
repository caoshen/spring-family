package com.example.multidatasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class
})
@Slf4j
public class MultiDatasourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDatasourceDemoApplication.class, args);
    }

	/**
	 * properties
	 *
	 * @return properties
	 */
	@Bean
	@ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
    	return new DataSourceProperties();
	}

	/**
	 * datasource
	 *
	 * @return datasource
	 */
	@Bean
	public DataSource fooDataSource() {
		DataSourceProperties properties = fooDataSourceProperties();
		System.out.println("foo datasource:" + properties.getUrl());
		return properties.initializeDataSourceBuilder().build();
	}

	/**
	 * transaction manager
	 *
	 * @return transaction manager
	 */
	@Bean
	@Resource
	public PlatformTransactionManager fooTxManger(DataSource fooDataSource) {
		return new DataSourceTransactionManager(fooDataSource);
	}

	@Bean
	@ConfigurationProperties("bar.datasource")
	public DataSourceProperties barDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource barDataSource() {
		DataSourceProperties properties = barDataSourceProperties();
		System.out.println("bar datasource:" + properties.getUrl());
		return properties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Resource
	public PlatformTransactionManager barTxManager(DataSource barDataSource) {
		return new DataSourceTransactionManager(barDataSource);
	}
}
