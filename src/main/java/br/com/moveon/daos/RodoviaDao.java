package br.com.moveon.daos;

import br.com.moveon.entites.Rodovia;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RodoviaDao {
    private JdbcTemplate jdbcTemplate;
    

    public RodoviaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Rodovia rodovia){

        this.jdbcTemplate.update("""
                INSERT INTO Rodovia 
                (idRodovia,nomeRodovia, denominacaoRodovia, 
                nomeConcessionaria, municipioRodovia, 
                regionalDer, regAdmMunicipio) VALUES (default, ?, ?, ?, ?, ?, ?)
                """, rodovia.getIdRodovia(), rodovia.getNomeRodovia(),
                rodovia.getDenominacaoRodovia(), rodovia.getNomeConcessionaria(), rodovia.getMunicipioRodovia(),
                rodovia.getRegionalDer(), rodovia.getMunicipioRodovia());

    }

    public void truncate() {
        this.jdbcTemplate.update("""
                TRUNCATE TABLE Rodovia
                """);
    }
    
    public Rodovia select(Rodovia rodovia) {
       List<Rodovia> rodovias = this.jdbcTemplate.query("""
                                       SELECT * FROM Rodovia where nomeRodovia = ? AND denominacaoRodovia = ? AND
                                       nomeConcessionaria = ? AND municipioRodovia = ? AND
                                       regionalDer = ? AND regAdmMunicipio = ?
                       """, rodovia.getIdRodovia(), rodovia.getNomeRodovia(),
                rodovia.getDenominacaoRodovia(), rodovia.getNomeConcessionaria(), rodovia.getMunicipioRodovia(),
                rodovia.getRegionalDer(), rodovia.getMunicipioRodovia(), new BeanPropertyRowMapper<>(Rodovia.class));
    if (rodovias.isEmpty()) {
        return null;
    }
    
    return  rodovias.get(0);
    }

}
