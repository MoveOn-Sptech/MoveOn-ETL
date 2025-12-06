package br.com.moveon.daos;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.entites.Acidente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class AcidenteDao extends EntityDao<Acidente> {

    @Override
    public void saveAll(List<Acidente> acidentes) throws SQLException {
        Connection conn = super.getBasicDataSource().getConnection();

        conn.setAutoCommit(false);
        try (
                PreparedStatement preparedStatement = conn.prepareStatement(this.query());
        ) {
            for (int i = 0; i < acidentes.size(); i++) {
                Acidente acidente = acidentes.get(i);

                preparedStatement.setDouble(1, acidente.getMarcoKm());
                preparedStatement.setTimestamp(2, Timestamp.valueOf(acidente.getDtHoraAcidente()));
                preparedStatement.setString(3, acidente.getTipoAcidente());
                preparedStatement.setString(4, acidente.getCausaAcidente());
                preparedStatement.setString(5, acidente.getClima().toString());
                preparedStatement.setInt(6, acidente.getQtdVitFatal());
                preparedStatement.setInt(7, acidente.getQtdVitGrave());
                preparedStatement.setInt(8, acidente.getQtdVitLeve());
                preparedStatement.setString(9, acidente.getTipoPista().toString());
                preparedStatement.setInt(10, acidente.getFkRodovia());
                preparedStatement.setInt(11, acidente.getFkConcessionaria());
                preparedStatement.setString(12, acidente.getVeiculosEnvolvidos());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }

    }

    @Override
    protected String query() {
        return """
                INSERT INTO Acidente (
                    idAcidente,
                    marcoKm,
                    dtHoraAcidente,
                    tipoAcidente,
                    causaAcidente,
                    clima,
                    qtdVitFatal,
                    qtdVitGrave,
                    qtdVitLeve,
                    tipoPista,
                    fkRodovia,
                    fkConcessionaria,
                    veiculosEnvolvidos
                )
                VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
    }
}
