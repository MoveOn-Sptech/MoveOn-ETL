package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;


public abstract class EntityDao<T> {
    private final JdbcTemplate jdbcTemplate;
    private final BasicDataSource basicDataSource;

    public EntityDao() {
        this.jdbcTemplate = DatabaseConnection.getInstance().getJdbcTemplate();
        this.basicDataSource = DatabaseConnection.getInstance().getBasicDataSource();
        }

    public abstract void saveAll(List<T> entities) throws SQLException;

    protected abstract String query();

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public BasicDataSource getBasicDataSource() {
        return basicDataSource;
    }
}
