package com.example.declarativetransactiondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@SpringBootTest
class DeclarativeTransactionDemoApplicationTests {
    @Autowired
    private FooService fooService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void invokeInsertThenRollbackBySelfService() {
        try {
            fooService.invokeInsertThenRollbackBySelfService();
        } catch (RollbackException e) {
            log.info("BBB {}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
        }
    }

    @Test
    void invokeInsertThenRollbackByAopContext() {
        try {
            fooService.invokeInsertThenRollbackByAopContext();
        } catch (RollbackException e) {
            log.info("BBB {}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
        }
    }

    @Test
    void invokeInsertThenRollbackAddTransactional() {
        try {
            fooService.invokeInsertThenRollbackAddTransactional();
        } catch (RollbackException e) {
            log.info("BBB {}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO WHERE BAR='BBB'", Long.class));
        }
    }

}
