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

    public static void main(String[] args) {
        DatabaseConnection connection = new DatabaseConnection();
        Logger logger = new Logger(connection.getJdbcTemplate());
        Workbook workbook = null;

        try {
            workbook = new XSSFWorkbook(
                    System.getenv("AWS_BUCKET_KEY_OBJECT")
            );
        } catch (Exception e) {
            logger.error("Infelizmente não foi possivel abrir o arquivo: " + System.getenv("AWS_BUCKET_KEY_OBJECT"));
            logger.info("Finalizando processo etl");
            System.exit(0);
        }

        logger.info("Iniciando processo ETL da base de dados da artesp:");

        S3Service s3Service = new S3Service(logger);
        try {
            s3Service.execute();
        } catch (Exception e) {
            logger.warn("Ops hove um erro: " + e.getMessage());
        }

        ETLService etlService = new ETLService(connection, logger, workbook);
        try {
            etlService.execute();
        } catch (Exception e) {
            logger.warn("Ops hove um erro: " + e.getMessage());
        }

        SlackService slackService = new SlackService();
        try {
            slackService.   sendMessageToChannel(
                    "#moveon-alerts",
                    "ETL finalizado com sucesso!"
            );

            slackService.sendDirectMessage(
                    "henry.arcaya@sptech.school",
                    "Processo concluído!"
            );
        } catch (Exception e) {
            logger.error("Infelizmente não foi possivel enviar email no slack");
            logger.info("Finalizando processo etl");
            System.exit(0);
        }

        logger.info(
                "Finalizando processo de extração dos acidentes da base de dados"
        );
        logger.info("Acidentes cadastradas com sucesso ");
        logger.info("Finalizando processo etl");
    }
}
