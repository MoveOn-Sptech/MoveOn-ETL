package br.com.moveon.connection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConnection {

    private final JdbcTemplate jdbcTemplate;
    private final BasicDataSource basicDataSource;

    public DatabaseConnection() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(System.getenv("DATABASE_URL"));
        basicDataSource.setUsername(System.getenv("DATABASE_USERNAME"));
        basicDataSource.setPassword(System.getenv("DATABASE_PASSWORD"));

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
