package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;


public abstract class EntityDao<T> {
    private JdbcTemplate jdbcTemplate;

    public EntityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public abstract void saveAll(List<T> entities, DatabaseConnection connection) throws SQLException;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
