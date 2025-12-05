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
        if (log.getIdLog() == null) {
            this.jdbcTemplate.update(
                    """
                            INSERT INTO Log(tipo, descricao, dataCriacao)
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
                        UPDATE Log SET
                            tipo =?
                            descricao = ?
                            dataCriacao =?
                        WHERE idLog = ?
                        """,
                log.getTipo(),
                log.getDescricao(),
                log.getDataCriacao(),
                log.getIdLog()
        );
    }


}
