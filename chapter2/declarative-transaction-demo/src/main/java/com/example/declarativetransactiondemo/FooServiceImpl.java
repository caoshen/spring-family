package com.example.declarativetransactiondemo;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FooServiceImpl implements FooService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FooService fooService;

    @Override
    public void insertRecord() {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('AAA')");
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void insertThenRollback() throws RollbackException {
        jdbcTemplate.execute("INSERT INTO FOO (BAR) VALUES ('BBB')");
        throw new RollbackException();
    }

    @Override
    public void invokeInsertThenRollback() throws RollbackException {
        insertThenRollback();
    }

    /**
     * use a instance
     *
     * @throws RollbackException
     */
    @Override
    public void invokeInsertThenRollbackBySelfService() throws RollbackException {
        fooService.insertThenRollback();
    }

    /**
     * use a proxy
     *
     * @throws RollbackException
     */
    @Override
    public void invokeInsertThenRollbackByAopContext() throws RollbackException {
        FooService service = (FooService) AopContext.currentProxy();
        service.insertThenRollback();
    }

    /**
     * use transactional annotation
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public void invokeInsertThenRollbackAddTransactional() throws RollbackException {
        insertThenRollback();
    }
}
