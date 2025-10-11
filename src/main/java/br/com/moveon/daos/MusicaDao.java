package br.com.moveon.daos;

import br.com.moveon.entites.Musica;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MusicaDao {


    private JdbcTemplate jdbcTemplate;

    public MusicaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(
            Musica musica
    ){
        this.jdbcTemplate.update("INSERT INTO musicas VALUE ( ?, ?, ?)", musica.getId(), musica.getTitulo(), musica.getDataLancamento());
    }

    public List<Musica> getAll(){
        List<Musica> musicas = this.jdbcTemplate.query("SELECT * FROM musicas", new BeanPropertyRowMapper<>(Musica.class));
        return musicas;
    }

    public void deleteAll(){
        this.jdbcTemplate.update("TRUNCATE musicas");
    }
}
