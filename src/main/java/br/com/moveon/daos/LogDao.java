package br.com.moveon.daos;

import br.com.moveon.entites.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class LogDao extends EntityDao<Log> {

    @Override
    public void saveAll(List<Log> logs) {
       try( Connection conn = super.getBasicDataSource().getConnection()) {
           conn.setAutoCommit(false);

           try (PreparedStatement preparedStatement = conn.prepareStatement(this.query());) {
               conn.setAutoCommit(false);

               for (Log log : logs) {
                   preparedStatement.setString(1, log.getTipo().name());
                   preparedStatement.setString(2, log.getDescricao());
                   preparedStatement.setTimestamp(3, Timestamp.from(log.getDataCriacao()));

                   preparedStatement.addBatch();
               }

               preparedStatement.execute();

               conn.commit();
           }   catch (Exception e) {
               e.printStackTrace();
           } finally {
               conn.setAutoCommit(true);
           }


       } catch (SQLException e) {
           e.printStackTrace();
       };

    }

    @Override
    protected String query() {
        return """
                 INSERT INTO Log(tipo, descricao, dataCriacao)
                                VALUES (?, ?, ?);
                """;
    }


}
