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
                regionalDer, regAdmMunicipio) VALUES (?, ?, ?, ?, ?, ?, ?)
                """,rodovia.getIdRodovia(), rodovia.getNomeRodovia(),rodovia.getDenominacaoRodovia(),
                rodovia.getNomeConcessionaria(), rodovia.getMunicipioRodovia(),
                rodovia.getRegionalDer(), rodovia.getMunicipioRodovia());
    }

    public void saveAll(List<Rodovia> rodovias){
        String query = """
        INSERT INTO Rodovia
        (idRodovia,nomeRodovia, denominacaoRodovia,nomeConcessionaria, municipioRodovia, regionalDer, regAdmMunicipio) VALUES """;

        String insertTeamplate = "(%d, '%s', '%s', '%s', '%s', '%s', '%s')";

        for (int i = 0; i < rodovias.size()-1; i++) {
            Rodovia rodovia = rodovias.get(i);
            query+=insertTeamplate.formatted(rodovia.getIdRodovia(), rodovia.getNomeRodovia().replaceAll("'", ""),rodovia.getDenominacaoRodovia().replaceAll("'", ""),
                    rodovia.getNomeConcessionaria().replaceAll("'", ""), rodovia.getMunicipioRodovia().replaceAll("'", ""),
                    rodovia.getRegionalDer().replaceAll("'", ""), rodovia.getMunicipioRodovia().replaceAll("'", "")).concat(",");
        }
        Rodovia rodovia = rodovias.getLast();

        query+=insertTeamplate.formatted(rodovia.getIdRodovia(), rodovia.getNomeRodovia(),rodovia.getDenominacaoRodovia(),
                rodovia.getNomeConcessionaria(), rodovia.getMunicipioRodovia(),
                rodovia.getRegionalDer(), rodovia.getMunicipioRodovia()).concat(";");


        System.out.println(query);
    }


    public void truncate() {
        this.jdbcTemplate.update("SET FOREIGN_KEY_CHECKS = 0");
        this.jdbcTemplate.update("DELETE FROM Acidente");
        this.jdbcTemplate.update("DELETE FROM Rodovia");
        this.jdbcTemplate.update("SET FOREIGN_KEY_CHECKS = 1");
    }
    
    public Rodovia select(Rodovia rodovia) {
       List<Rodovia> rodovias = this.jdbcTemplate.query("""
                        SELECT * FROM Rodovia where nomeRodovia = ? AND denominacaoRodovia = ? AND
                        nomeConcessionaria = ? AND municipioRodovia = ? AND
                        regionalDer = ? AND regAdmMunicipio = ?
                       """,new BeanPropertyRowMapper<>(Rodovia.class),
                            rodovia.getNomeRodovia(),
                            rodovia.getDenominacaoRodovia(),
                            rodovia.getNomeConcessionaria(),
                            rodovia.getMunicipioRodovia(),
                            rodovia.getRegionalDer(),
                            rodovia.getMunicipioRodovia());

       if (rodovias.isEmpty()) {
        return null;
    }
    
    return  rodovias.get(0);
    }

}
