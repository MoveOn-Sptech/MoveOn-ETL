package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.daos.AcidenteDao;
import br.com.moveon.daos.RodoviaDao;
import br.com.moveon.entites.Acidente;
import br.com.moveon.entites.Rodovia;
import br.com.moveon.providers.Logger;
import br.com.moveon.providers.S3Provider;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        DatabaseConnection connection = new DatabaseConnection();
        RodoviaDao rodoviaDao = new RodoviaDao(connection.getJdbcTemplate());

        Logger logger = new Logger(connection.getJdbcTemplate());

        logger.info("Iniciando processo ETL da base de dados da artesp:");


        S3Client s3Client = new S3Provider().getS3Client();
        String bucketName = "henry-franz-ramos-arcaya-2025-2006";
        String keyObject = "2024_dezmil.xlsx";
        logger.info("Estabelecendo conexão com a AWS BUCKET: " + bucketName);


        logger.info("Realizando download do arquivo " + keyObject + " da base da dados do bucket " + bucketName);
        //        BAIXANDO ARQUIVOS
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(keyObject).build();

        InputStream stream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
        boolean fileExists = new File(getObjectRequest.key()).exists();

        if (!fileExists)
            Files.copy(stream, new File(getObjectRequest.key()).toPath());


        Workbook workbook = new XSSFWorkbook("./" + keyObject);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();


        rowIterator.next();
        rodoviaDao.truncate();

        Integer idRodovia = 1;
        Integer rodoviasNaoValidas = 0;

        HashMap<Rodovia, Integer> rodovias = new HashMap<>();

        logger.info("Iniciando processo de extração das rodovias da base de dados");

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

//            PRIORIDADE NOME DA CONCESSONARIA E DA RODOVIA
            if (
                    row.getCell(2) != null &&
                    row.getCell(1) != null
            ) {

                Rodovia rodovia = new Rodovia(row);
                boolean existeRodovia = rodovias.get(rodovia) == null;

                if (existeRodovia) {
                    rodovias.put(rodovia, idRodovia);

                    rodovia.setIdRodovia(idRodovia);
                    rodoviaDao.save(rodovia);
                    idRodovia++;
                }
            } else {
                rodoviasNaoValidas++;
            }

        }
        logger.info("Rodovias cadastradas com sucesso ao todo foram " + idRodovia + " cadastradas e " + rodoviasNaoValidas + " não cadastradas");
        logger.info("Finalizando processo de extração das rodovias da base de dados");


        AcidenteDao acidenteDao = new AcidenteDao(connection.getJdbcTemplate());

        Iterator<Row> rowIteratorAcidente = sheet.rowIterator();
        rowIteratorAcidente.next();

        logger.info("Iniciando processo de extração dos acidentes da base de dados");
        while (rowIteratorAcidente.hasNext()) {
            Row row = rowIteratorAcidente.next();
            Rodovia rodovia = new Rodovia(row);
            rodovia.setIdRodovia(rodovias.get(rodovia));

            if (
                    row.getCell(0) != null &&
                    row.getCell(3) != null &&
                    row.getCell(5) != null &&
                    row.getCell(7) != null &&
                    row.getCell(6) != null &&
                    row.getCell(8) != null &&
                    row.getCell(10) != null &&
                    row.getCell(14) != null &&
                    row.getCell(15) != null &&
                    row.getCell(16) != null &&
                    row.getCell(19) != null
            ) {
                if (row.getRowNum() % 1000 == 0) {
                    logger.info("Lendo da linha " + (row.getRowNum() - 1000) + " Até " + row.getRowNum());
                }
                Acidente acidente = new Acidente(row, rodovia);
                acidenteDao.save(acidente);
            }
        }
        logger.info("Finalizando processo de extração dos acidentes da base de dados");
        logger.info("Acidentes cadastradas com sucesso ");
        logger.info("Finalizando processo etl");
    }

}
