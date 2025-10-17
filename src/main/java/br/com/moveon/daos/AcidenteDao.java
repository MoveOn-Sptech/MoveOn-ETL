package br.com.moveon.daos;

import br.com.moveon.entites.Acidente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AcidenteDao {

    private JdbcTemplate jdbcTemplate;

    public AcidenteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Acidente acidente) {
        this.jdbcTemplate.update("""
                        INSERT INTO Acidente (
                            idAcidente,
                            marcoKm,
                            dtHoraAcidente,
                            tipoAcidente,
                            causaAcidente,
                            clima,
                            veiculosEnvolvidos,
                            vitFatal,
                            vitGrave,
                            vitLeve,
                            tipoPista,
                            fkRodovia
                        )
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """,
                acidente.getIdAcidente(),
                acidente.getMarcoKm(),
                acidente.getDtHoraAcidente(),
                acidente.getTipoAcidente(),
                acidente.getCausaAcidente(),
                acidente.getClima(),
                acidente.getVeiculosEnvolvidos(),
                acidente.getVitFatal(),
                acidente.getVitGrave(),
                acidente.getVitLeve(),
                acidente.getTipoPista(),
                acidente.getFkRodovia()
        );

    }

    public List<Acidente> findAll(){
        List<Acidente> acidentes = this.jdbcTemplate.query("SELECT * FROM Acidente", new BeanPropertyRowMapper<>(Acidente.class));

        return acidentes;
    }
}
