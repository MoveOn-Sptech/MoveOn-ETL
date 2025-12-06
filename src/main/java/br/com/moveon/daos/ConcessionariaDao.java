package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.entites.Concessionaria;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ConcessionariaDao extends EntityDao<Concessionaria> {


    public void truncate() {
        this.getJdbcTemplate().update("SET FOREIGN_KEY_CHECKS = 0");
        this.getJdbcTemplate().update("DELETE FROM Concessionaria");
        this.getJdbcTemplate().update("DELETE FROM Acidente");
        this.getJdbcTemplate().update("DELETE FROM Rodovia");
        this.getJdbcTemplate().update("SET FOREIGN_KEY_CHECKS = 1");
    }


    @Override
    public void saveAll(List<Concessionaria> concessionarias) throws SQLException {
        Connection conn = super.getBasicDataSource().getConnection();
        conn.setAutoCommit(false);

        try (PreparedStatement preparedStatement = conn.prepareStatement(this.query());) {
            for (Concessionaria concessionaria : concessionarias) {
                preparedStatement.setInt(1, concessionaria.getIdConcessionaria());
                preparedStatement.setString(2, concessionaria.getNome());

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

    @Override
    protected String query() {
        return """
                INSERT INTO Concessionaria (idConcessionaria, nome)
                     VALUES (?, ?)
                """;
    }


}
