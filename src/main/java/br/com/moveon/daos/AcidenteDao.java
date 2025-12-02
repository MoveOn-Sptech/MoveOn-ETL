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

    private JdbcTemplate jdbcTemplate;

    public AcidenteDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void saveAll(List<Acidente> acidentes, DatabaseConnection connection) throws SQLException {
        Connection conn = connection.getBasicDataSource().getConnection();

        conn .setAutoCommit(false);

        String query= """
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
                """;

        try (
                PreparedStatement preparedStatement = conn .prepareStatement(query);
                ){
            for (Acidente acidente : acidentes) {
                preparedStatement.setInt(1, acidente.getIdAcidente());
                preparedStatement.setDouble(2, acidente.getMarcoKm());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(acidente.getDtHoraAcidente()));
                preparedStatement.setString(4, acidente.getTipoAcidente());
                preparedStatement.setString(5, acidente.getCausaAcidente());
                preparedStatement.setString(6, acidente.getClima().toString());
                preparedStatement.setString(7, acidente.getVeiculosEnvolvidos());
                preparedStatement.setInt(8, acidente.getVitFatal());
                preparedStatement.setInt(9, acidente.getVitGrave());
                preparedStatement.setInt(10, acidente.getVitLeve());
                preparedStatement.setString(11, acidente.getTipoPista().toString());
                preparedStatement.setInt(12, acidente.getFkRodovia());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            conn .commit();
        }catch (Exception e){
            conn .rollback();
            e.printStackTrace();
        }finally {
            conn .setAutoCommit(true);
        }

    }
}
