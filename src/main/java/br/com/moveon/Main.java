package br.com.moveon;

import br.com.moveon.daos.MusicaDao;
import br.com.moveon.entites.Log;
import br.com.moveon.entites.Musica;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        Logger logger = new Logger();
        Workbook workbook = new XSSFWorkbook("./musicas.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();

        logger.info("Iniciando processo de ETL da artesp");
        // hasNext => existe um proximo
        // next => item em si
        MusicaDao musicaDao = new MusicaDao(logger.getDatabaseConnection().getJdbcTemplate());
        logger.info("Deletando base de dados");
        musicaDao.deleteAll();

        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Double id = Double.valueOf(row.getCell(0).toString());
            String titulo = row.getCell(1).toString();
            LocalDate localDate = row.getCell(10).getLocalDateTimeCellValue().toLocalDate();


            Musica musica = new Musica(id, titulo, localDate);
            musicaDao.save(musica);

        }

        logger.info("Processo de extração e tranformação feito com sucesso");


//        CRIAÇÂO DE PLANILHA
        Workbook workbookTransformado = new XSSFWorkbook();
        Sheet sheetTranformado = workbookTransformado.createSheet();

        List<Musica> musicas = musicaDao.getAll();

        for (int i = 0; i < musicas.size(); i++) {
            Musica musica = musicas.get(i);
            Row row = sheetTranformado.createRow(i);

            row.createCell(0).setCellValue(musica.getId());
            row.createCell(1).setCellValue(musica.getTitulo());
            row.createCell(2).setCellValue(musica.getDataLancamento().toString());
        }

        FileOutputStream outputStream = new FileOutputStream("./data2.xlsx");
        workbookTransformado.write(outputStream);

        logger.info("Finalização do processo etl");

    }

}




