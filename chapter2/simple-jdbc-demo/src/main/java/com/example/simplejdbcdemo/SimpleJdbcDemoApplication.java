package com.example.simplejdbcdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
public class SimpleJdbcDemoApplication implements CommandLineRunner {
	@Autowired
	private FooDao fooDao;

	@Autowired
	private BatchFooDao batchFooDao;

	public static void main(String[] args) {
		SpringApplication.run(SimpleJdbcDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fooDao.insertData();
		batchFooDao.batchInsert();
		fooDao.listData();
	}

	@Bean
	@Autowired
	public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate) {
		return new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("FOO")
				.usingGeneratedKeyColumns("ID");
	}

	@Bean
	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
