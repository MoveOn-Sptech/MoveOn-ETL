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
            for (int i = 0; i < acidentes.size(); i++) {
                Acidente acidente = acidentes.get(i);
                preparedStatement.setInt(1, acidente.getIdAcidente());
                preparedStatement.setDouble(2, acidente.getMarcoKm());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(acidente.getDtHoraAcidente()));
                preparedStatement.setString(4, acidente.getTipoAcidente());
                preparedStatement.setString(5, acidente.getCausaAcidente());
                preparedStatement.setString(6, acidente.getClima());
                preparedStatement.setString(7, acidente.getVeiculosEnvolvidos());
                preparedStatement.setInt(8, acidente.getVitFatal());
                preparedStatement.setInt(9, acidente.getVitGrave());
                preparedStatement.setInt(10, acidente.getVitLeve());
                preparedStatement.setString(11, acidente.getTipoPista());
                preparedStatement.setInt(12, acidente.getFkRodovia());

                if(i % 1000 == 0){
                    preparedStatement.executeBatch();
                }

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

    public List<Acidente> findAll(){
        List<Acidente> acidentes = this.jdbcTemplate.query("SELECT * FROM Acidente", new BeanPropertyRowMapper<>(Acidente.class));

        return acidentes;
    }
}
