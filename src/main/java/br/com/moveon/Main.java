package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.daos.AcidenteDao;
import br.com.moveon.daos.RodoviaDao;
import br.com.moveon.entites.Acidente;
import br.com.moveon.entites.Rodovia;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        DatabaseConnection connection = new DatabaseConnection();
        RodoviaDao rodoviaDao = new RodoviaDao(connection.getJdbcTemplate());

        Logger logger = new Logger(connection.getJdbcTemplate());

        logger.info("Iniciando processo ETL:");

        S3Client s3Client = new S3Provider().getS3Client();
        String bucketName = "s3-moveon-prod-source";

        logger.info("Estabelecendo conexão com a AWS BUCKET: " + bucketName);
//        //                CRIANDO BUCKET
//        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build();
//        s3Client.createBucket(createBucketRequest);

        logger.info("Baixando arquivos da base da dados: ");

        //        BAIXANDO ARQUIVOS
        ListObjectsRequest listObjectsRequest1 = ListObjectsRequest.builder().bucket(bucketName).build();
        List<S3Object> s3Objects1 = s3Client.listObjects(listObjectsRequest1).contents();
        for (S3Object s3Object : s3Objects1) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build();

            InputStream stream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
            boolean fileExists = new File(s3Object.key()).exists();
            if (!fileExists)
                Files.copy(stream, new File(s3Object.key()).toPath());

        }

        Workbook workbook = new XSSFWorkbook("./2024_cem.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();


        rowIterator.next();
        rodoviaDao.truncate();

        Integer idRodovia = 1;
        Integer rodoviasNaoValidas = 0;

        List<Rodovia> rodovias = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (
                    row.getCell(2) != null &&
                    row.getCell(1) != null
            ) {

                Rodovia rodovia = obterRodovia(row);
                Rodovia rodoviaEncontrada = rodoviaDao.select(rodovia);

                if (rodoviaEncontrada == null) {
                    rodovia.setIdRodovia(idRodovia);
                    rodoviaDao.save(rodovia);
                    rodovias.add(rodovia);
                    idRodovia++;
                }
            } else {
                rodoviasNaoValidas++;
            }

        }
        System.out.println(rodovias);
        logger.info("Rodovias cadastradas com sucesso ao todo foram " + idRodovia + " cadastradas e " + rodoviasNaoValidas + " não cadastradas");


        AcidenteDao acidenteDao = new AcidenteDao(connection.getJdbcTemplate());

        Iterator<Row> rowIteratorAcidente = sheet.rowIterator();
        Integer quantidade = 0;
        rowIteratorAcidente.next();
        while (rowIteratorAcidente.hasNext()) {
            Row row = rowIteratorAcidente.next();
            Rodovia rodovia = obterRodovia(row);


            for (Rodovia rodoviaAtual : rodovias) {
                if (
                        rodoviaAtual.getNomeRodovia().equals(rodovia.getNomeRodovia()) &&
                        rodoviaAtual.getDenominacaoRodovia().equals(rodovia.getDenominacaoRodovia()) &&
                        rodoviaAtual.getNomeConcessionaria().equals(rodovia.getNomeConcessionaria()) &&
                        rodoviaAtual.getMunicipioRodovia().equals(rodovia.getMunicipioRodovia()) &&
                        rodoviaAtual.getRegionalDer().equals(rodovia.getRegionalDer()) &&
                        rodoviaAtual.getRegAdmMunicipio().equals(rodovia.getRegAdmMunicipio())
                ) {
                    rodovia.setIdRodovia(rodoviaAtual.getIdRodovia());
                    quantidade++;

                    break;
                }
            }

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
                String dataString = row.getCell(5).toString();
                System.out.println(dataString);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dataFormatada = LocalDateTime.parse(dataString, formatter);
                System.out.println(dataFormatada);
                Acidente acidente = new Acidente(
                        (int) row.getCell(0).getNumericCellValue(),
                        row.getCell(3).getNumericCellValue(),
                        dataFormatada,
                        row.getCell(7).toString(),
                        row.getCell(6).toString(),
                        row.getCell(8).toString(),
                        row.getCell(10).toString(),
                        (int) row.getCell(14).getNumericCellValue(),
                        (int) row.getCell(15).getNumericCellValue(),
                        (int) row.getCell(16).getNumericCellValue(),
                        row.getCell(19).toString(),
                        rodovia.getIdRodovia()

                );

                acidenteDao.save(acidente);
            }

        }


    }

    static Rodovia obterRodovia(Row row) {
        return new Rodovia(
                row.getCell(2).toString(), // nomeRodovia
                row.getCell(20) != null ? row.getCell(20).toString() : "", //denominacaoRodovia
                row.getCell(1).toString(), //nomeConcessionaria
                row.getCell(21) != null ? row.getCell(21).toString() : "", //municipioRodovia
                row.getCell(22) != null ? row.getCell(22).toString() : "", //regionalDer
                row.getCell(22) != null ? row.getCell(23).toString() : "" //regAdmMunicipio
        );
    }
}
