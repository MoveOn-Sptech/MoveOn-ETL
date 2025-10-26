package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.entites.Rodovia;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RodoviaDao {
    private JdbcTemplate jdbcTemplate;


    public RodoviaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Rodovia rodovia) {
        this.jdbcTemplate.update("""
                        INSERT INTO Rodovia 
                        (idRodovia,nomeRodovia, denominacaoRodovia, 
                        nomeConcessionaria, municipioRodovia, 
                        regionalDer, regAdmMunicipio) VALUES (?, ?, ?, ?, ?, ?, ?)
                        """, rodovia.getIdRodovia(), rodovia.getNomeRodovia(), rodovia.getDenominacaoRodovia(),
                rodovia.getNomeConcessionaria(), rodovia.getMunicipioRodovia(),
                rodovia.getRegionalDer(), rodovia.getMunicipioRodovia());
    }

    public void saveAll(List<Rodovia> rodovias, DatabaseConnection connection) throws SQLException {
        Connection conn = connection.getBasicDataSource().getConnection();
        conn.setAutoCommit(false);

        String query = """
                INSERT INTO Rodovia (idRodovia,nomeRodovia, denominacaoRodovia, nomeConcessionaria, municipioRodovia, regionalDer, regAdmMunicipio)
                     VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            for (Rodovia rodovia : rodovias) {
                preparedStatement.setInt(1, rodovia.getIdRodovia());
                preparedStatement.setString(2, rodovia.getNomeRodovia());
                preparedStatement.setString(3, rodovia.getDenominacaoRodovia());
                preparedStatement.setString(4, rodovia.getNomeConcessionaria());
                preparedStatement.setString(5, rodovia.getMunicipioRodovia());
                preparedStatement.setString(6, rodovia.getRegionalDer());
                preparedStatement.setString(7, rodovia.getRegAdmMunicipio());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            conn.commit();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }

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
                        """, new BeanPropertyRowMapper<>(Rodovia.class),
                rodovia.getNomeRodovia(),
                rodovia.getDenominacaoRodovia(),
                rodovia.getNomeConcessionaria(),
                rodovia.getMunicipioRodovia(),
                rodovia.getRegionalDer(),
                rodovia.getMunicipioRodovia());

        if (rodovias.isEmpty()) {
            return null;
        }

        return rodovias.get(0);
    }

}
