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
        rodoviaDao.truncate();

        Integer rodoviasValidas = 0;
        Integer rodoviasNaoValidas = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (
                    row.getCell(2) != null &&
                    row.getCell(20) != null &&
                    row.getCell(1) != null &&
                    row.getCell(21) != null &&
                    row.getCell(22) != null &&
                    row.getCell(23) != null
            ) {

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
                    rodoviasValidas++;
                }
            }else {
                rodoviasNaoValidas++;
            }

        }

            logger.info("Rodovias cadastradas com sucesso ao todo foram "+ rodoviasValidas+" cadastradas e "+ rodoviasNaoValidas+" não cadastradas");


    }

}
