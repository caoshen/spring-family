package com.example.simplejdbcdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BatchFooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void batchInsert() {
        jdbcTemplate.batchUpdate("INSERT INTO FOO (BAR) VALUES (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, "b-" +i);
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                }
        );

        List<Foo> list = new ArrayList<>();
        list.add(new Foo(100L, "b-100"));
        list.add(new Foo(101L, "b-101"));
        namedParameterJdbcTemplate
                .batchUpdate("INSERT INTO FOO (ID, BAR) VALUES (:id, :bar)",
                        SqlParameterSourceUtils.createBatch(list));
    }
}
