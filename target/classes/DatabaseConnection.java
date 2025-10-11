package br.com.moveon.connection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConnection {

    private final JdbcTemplate jdbcTemplate;
    private final BasicDataSource basicDataSource;

    public DatabaseConnection() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://localhost:3307/dao");
        basicDataSource.setUsername("sa");
        basicDataSource.setPassword("");

        this.basicDataSource = basicDataSource;
        this.jdbcTemplate = new JdbcTemplate(basicDataSource);
    }

    public BasicDataSource getBasicDataSource() {
        return basicDataSource;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
