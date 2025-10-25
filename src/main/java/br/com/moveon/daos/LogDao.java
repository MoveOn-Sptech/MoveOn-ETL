package br.com.moveon.daos;

import br.com.moveon.entites.Log;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class LogDao {

    private JdbcTemplate jdbcTemplate;

    public LogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Log log) {
        if (log.getId() == null) {
            this.jdbcTemplate.update(
                    """
                            INSERT INTO logs(tipo, descricao, dataCriacao)
                                VALUES (?, ?, ?);
                            """,
                    log.getTipo(),
                    log.getDescricao(),
                    log.getDataCriacao()
            );
            return;
        }

        this.jdbcTemplate.update(
                """
                        UPDATE logs SET
                            tipo =?
                            descricao = ?
                            dataCriacao =?
                        WHERE id = ?
                        """,
                log.getTipo(),
                log.getDescricao(),
                log.getDataCriacao(),
                log.getId()
        );
    }

    public List<Log> getAll(){
        return this.jdbcTemplate.query("SELECT * FROM logs ORDER BY id DESC", new BeanPropertyRowMapper<>(Log.class));
    }

}
