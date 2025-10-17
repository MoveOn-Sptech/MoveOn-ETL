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
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
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

        Workbook workbook = new XSSFWorkbook("./2024.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();


        rowIterator.next();
        rodoviaDao.truncate();

        Integer idRodovia = 1;
        Integer rodoviasNaoValidas = 0;

        List<Rodovia> rodovias = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

//            PRIORIDADE NOME DA CONCESSONARIA E DA RODOVIA
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dataFormatada = LocalDateTime.parse(dataString, formatter);
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

        Workbook workbookDist = new XSSFWorkbook();
        Sheet sheeRodoviatDist = workbookDist.createSheet("rodovias");

        logger.info("Exportando base de dados");
        Row rowHeader = sheeRodoviatDist.createRow(0);
        rowHeader.createCell(0).setCellValue("ID");
        rowHeader.createCell(1).setCellValue("RODOVIA");
        rowHeader.createCell(2).setCellValue("DENOMINACAO");
        rowHeader.createCell(3).setCellValue("NOME_CONC");
        rowHeader.createCell(4).setCellValue("MUNICÍPIO");
        rowHeader.createCell(5).setCellValue("REGIONAL_DER");
        rowHeader.createCell(6).setCellValue("REG_ADM_SP");

        for (int i = 1; i < rodovias.size() + 1; i++) {
            Row row = sheeRodoviatDist.createRow(i);

            Rodovia rodovia = rodovias.get(i - 1);
            row.createCell(0).setCellValue(rodovia.getIdRodovia());
            row.createCell(1).setCellValue(rodovia.getNomeRodovia());
            row.createCell(2).setCellValue(rodovia.getDenominacaoRodovia());
            row.createCell(3).setCellValue(rodovia.getNomeConcessionaria());
            row.createCell(4).setCellValue(rodovia.getMunicipioRodovia());
            row.createCell(5).setCellValue(rodovia.getRegionalDer());
            row.createCell(6).setCellValue(rodovia.getRegAdmMunicipio());
        }

        Sheet sheetAcidentesDist = workbookDist.createSheet("acidentes");
        Row rowAcidentesHeader = sheetAcidentesDist.createRow(0);
        rowAcidentesHeader.createCell(0).setCellValue("ID");
        rowAcidentesHeader.createCell(1).setCellValue("MARCO_QM");
        rowAcidentesHeader.createCell(2).setCellValue("DTHR_OC");
        rowAcidentesHeader.createCell(3).setCellValue("TIPO_ACID");
        rowAcidentesHeader.createCell(4).setCellValue("CLASS_ACID");
        rowAcidentesHeader.createCell(5).setCellValue("METEORO");
        rowAcidentesHeader.createCell(6).setCellValue("VEIC");
        rowAcidentesHeader.createCell(7).setCellValue("VIT_FATAL_INT");
        rowAcidentesHeader.createCell(8).setCellValue("VIT_MODERADO_INT");
        rowAcidentesHeader.createCell(9).setCellValue("VIT_LEVE_INT");
        rowAcidentesHeader.createCell(10).setCellValue("TIPO_PIST");
        rowAcidentesHeader.createCell(12).setCellValue("FK_RODOVIA");

        List<Acidente> acidentes = acidenteDao.findAll();
        for (int i = 1; i < acidentes.size() + 1; i++) {
            Row row = sheetAcidentesDist.createRow(i);

            Acidente acidente = acidentes.get(i - 1);
            row.createCell(0).setCellValue(acidente.getIdAcidente());
            row.createCell(1).setCellValue(acidente.getMarcoKm());
            row.createCell(2).setCellValue(acidente.getDtHoraAcidente());
            row.createCell(3).setCellValue(acidente.getTipoAcidente());
            row.createCell(4).setCellValue(acidente.getCausaAcidente());
            row.createCell(5).setCellValue(acidente.getClima());
            row.createCell(6).setCellValue(acidente.getVeiculosEnvolvidos());
            row.createCell(7).setCellValue(acidente.getVitFatal());
            row.createCell(8).setCellValue(acidente.getVitGrave());
            row.createCell(9).setCellValue(acidente.getVitLeve());
            row.createCell(10).setCellValue(acidente.getTipoPista());
            row.createCell(12).setCellValue(acidente.getFkRodovia());

        }


        logger.info("Exportação realizado com sucesso");

        String filename = "dist-2024.xlsx";
        FileOutputStream outputStream = new FileOutputStream(filename);
        workbookDist.write(outputStream);


        bucketName = "s3-moveon-prod-dist";
//        UPLOAD ARQUIVO LOCAL
        logger.info("Realizando upload de arquivo tratado");

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(filename).build();
        File file = new File(filename);
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

        logger.info("Finalizando processo etl");
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
