package com.example.datasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
@Slf4j
public class DatasourceDemoApplication implements CommandLineRunner {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DatasourceDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		showConnection();
		showData();
	}

	private void showData() {
		jdbcTemplate.queryForList("SELECT * FROM FOO")
//				print every row in table
				.forEach(stringObjectMap -> System.out.println(stringObjectMap.toString()));
	}

	private void showConnection() throws SQLException {
		String datasource = dataSource.toString();
		System.out.println(datasource);
		Connection connection = dataSource.getConnection();
		System.out.println(connection.toString());
		connection.close();
	}
}
