package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.providers.Logger;
import br.com.moveon.services.ETLService;
import br.com.moveon.services.S3Service;
import br.com.moveon.services.SlackService;

import java.io.*;

import br.com.moveon.services.utils.SlackDefaultMessages;

public class Main {


    public static void main(String[] args) throws IOException {
        DatabaseConnection connection = new DatabaseConnection();
        Logger logger = new Logger(connection.getJdbcTemplate());
        SlackService slackService = new SlackService(logger);

        logger.info("Iniciando processo ETL da base de dados da artesp:");

        S3Service s3Service = new S3Service(logger);
        s3Service.execute();

        ETLService etlService = new ETLService(logger, connection, slackService);
        etlService.execute();

        slackService.sendMessageToChannel("#moveon-alerts", SlackDefaultMessages.SUCCESS_PROCESS);
        slackService.sendDirectMessage("henry.arcaya@sptech.school", SlackDefaultMessages.SUCCESS_PROCESS);

        logger.info("Finalizando processo etl");
    }
}
