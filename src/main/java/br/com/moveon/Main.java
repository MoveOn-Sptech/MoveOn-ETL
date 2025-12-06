package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.services.utils.Logger;
import br.com.moveon.services.ETLService;
import br.com.moveon.services.S3Service;
import br.com.moveon.services.SlackService;

import java.io.*;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getInstance();
        SlackService slackService = new SlackService();

        logger.info("Iniciando processo ETL da base de dados da artesp:");

        S3Service s3Service = new S3Service();
        List<String> filenames = s3Service.downloadAllFiles();

        ETLService etlService = new ETLService(filenames, slackService);
        etlService.execute();

        logger.info("Finalizando processo ETL da base de dados da artesp.");
        logger.saveAllLogs();
    }
}
