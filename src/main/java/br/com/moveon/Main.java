package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.providers.Logger;
import br.com.moveon.services.ETLService;
import br.com.moveon.services.S3Service;
import br.com.moveon.services.SlackService;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

    private static final Log log = LogFactory.getLog(Main.class);

    public static void main(String[] args)
            throws IOException, InterruptedException {
        DatabaseConnection connection = new DatabaseConnection();
        Logger logger = new Logger(connection.getJdbcTemplate());

        Workbook workbook = new XSSFWorkbook(
                System.getenv("AWS_BUCKET_KEY_OBJECT")
        );

        logger.info("Iniciando processo ETL da base de dados da artesp:");

        try {
            S3Service s3Service = new S3Service(logger);
            s3Service.execute();
        } catch (Exception e) {
            logger.warn("Ops hove um erro: " + e.getMessage());
        }

        try {
            ETLService etlService = new ETLService(connection, logger, workbook);
            etlService.execute();
        } catch (Exception e) {
            logger.warn("Ops hove um erro: " + e.getMessage());
        }

        SlackService slackService = new SlackService();

        slackService.sendMessageToChannel(
                "#moveon-alerts",
                "ETL finalizado com sucesso!"
        );

        slackService.sendDirectMessage(
                "henry.arcaya@sptech.school",
                "Processo concluído!"
        );

        logger.info(
                "Finalizando processo de extração dos acidentes da base de dados"
        );
        logger.info("Acidentes cadastradas com sucesso ");
        logger.info("Finalizando processo etl");
    }
}
