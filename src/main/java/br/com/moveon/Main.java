package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.providers.Logger;
import br.com.moveon.services.ETLService;
import br.com.moveon.services.S3Service;
import br.com.moveon.services.SlackService;

import java.io.*;
import java.util.List;

import br.com.moveon.services.utils.SlackDefaultMessages;

public class Main {


    public static void main(String[] args) throws IOException {
        DatabaseConnection connection = new DatabaseConnection();
        Logger logger = new Logger(connection.getJdbcTemplate());
        SlackService slackService = new SlackService(logger);

        logger.info("Iniciando processo ETL da base de dados da artesp:");

        S3Service s3Service = new S3Service(logger);
        List<String> filenames = s3Service.downloadAllFiles();

        ETLService etlService = new ETLService(filenames, logger, connection, slackService);
        etlService.execute();

        logger.info("Finalizando processo etl");
    }
}
