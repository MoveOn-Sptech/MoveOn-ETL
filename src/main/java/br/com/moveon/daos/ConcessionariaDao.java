package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.entites.Concessionaria;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ConcessionariaDao {
    private JdbcTemplate jdbcTemplate;


    public ConcessionariaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void truncate() {
        this.jdbcTemplate.update("SET FOREIGN_KEY_CHECKS = 0");
        this.jdbcTemplate.update("DELETE FROM Concessionaria");
        this.jdbcTemplate.update("DELETE FROM Acidente");
        this.jdbcTemplate.update("DELETE FROM Rodovia");
        this.jdbcTemplate.update("SET FOREIGN_KEY_CHECKS = 1");
    }



    public void saveAll(List<Concessionaria> concessionarias, DatabaseConnection connection) throws SQLException {
        Connection conn = connection.getBasicDataSource().getConnection();
        conn.setAutoCommit(false);

        String query = """
                INSERT INTO Concessionaria (idConcessionaria, nomeConcessionaria)
                     VALUES (?, ?)
                """;
        try (PreparedStatement preparedStatement = conn.prepareStatement(query);) {
            for (Concessionaria concessionaria : concessionarias) {
                preparedStatement.setInt(1, concessionaria.getIdConcessionaria());
                preparedStatement.setString(2, concessionaria.getNomeConcessionaria());

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
