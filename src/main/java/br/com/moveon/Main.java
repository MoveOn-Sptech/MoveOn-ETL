package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.daos.RodoviaDao;
import br.com.moveon.entites.Rodovia;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws IOException {

        DatabaseConnection connection = new DatabaseConnection();
        RodoviaDao rodoviaDao = new RodoviaDao(connection.getJdbcTemplate());

        Logger logger = new Logger(connection.getJdbcTemplate());
        Workbook workbook = new XSSFWorkbook("./2024.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();

        logger.info("Iniciando processo de ETL da artesp");

       rowIterator.next();

        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            Rodovia rodovia = new Rodovia(
                    row.getCell(2).toString(),
                    row.getCell(20).toString(),
                    row.getCell(1).toString(),
                    row.getCell(21).toString(),
                    row.getCell(22).toString(),
                    row.getCell(23).toString()

            );

            Rodovia rodoviaEncontrada = rodoviaDao.select(rodovia);

            if (rodoviaEncontrada == null) {
                rodoviaDao.save(rodovia);
            }

        }

    }

}
