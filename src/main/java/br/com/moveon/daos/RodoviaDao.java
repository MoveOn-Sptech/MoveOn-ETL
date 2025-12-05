package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.entites.Concessionaria;
import br.com.moveon.entites.Rodovia;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RodoviaDao extends EntityDao<Rodovia> {

    public RodoviaDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void saveAll(List<Rodovia> rodovias, DatabaseConnection connection) throws SQLException {
        Connection conn = connection.getBasicDataSource().getConnection();
        conn.setAutoCommit(false);

        String query = """
                INSERT INTO Rodovia (idRodovia, nome, denominacao, municipio, regionalDer, regionalAdmSp, fkConcessionaria)
                     VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            for (Rodovia rodovia : rodovias) {
                preparedStatement.setInt(1, rodovia.getIdRodovia());
                preparedStatement.setString(2, rodovia.getNome());
                preparedStatement.setString(3, rodovia.getDenominacao());
                preparedStatement.setString(4, rodovia.getMunicipio());
                preparedStatement.setString(5, rodovia.getRegionalDer());
                preparedStatement.setString(6, rodovia.getRegionalAdmSp());
                preparedStatement.setInt(7, rodovia.getFkConcessionaria());
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
}
