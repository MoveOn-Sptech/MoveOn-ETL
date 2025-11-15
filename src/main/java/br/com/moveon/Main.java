package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.providers.Logger;
import br.com.moveon.services.ETLService;
import br.com.moveon.services.S3Service;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        DatabaseConnection connection = new DatabaseConnection();
        Logger logger = new Logger(connection.getJdbcTemplate());

        Workbook workbook = new XSSFWorkbook(System.getenv("AWS_BUCKET_KEY_OBJECT"));

        logger.info("Iniciando processo ETL da base de dados da artesp:");

        S3Service s3Service = new S3Service(logger);
        s3Service.execute();

        ETLService etlService = new ETLService(connection, logger, workbook);
        etlService.execute();

        logger.info("Finalizando processo de extração dos acidentes da base de dados");
        logger.info("Acidentes cadastradas com sucesso ");
        logger.info("Finalizando processo etl");
    }
}
