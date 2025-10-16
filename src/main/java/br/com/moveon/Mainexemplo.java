package br.com.moveon;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.daos.RodoviaDao;
import br.com.moveon.entites.Rodovia;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mainexemplo {

    public static void main(String[] args) throws IOException {

        DatabaseConnection connection = new DatabaseConnection();
        RodoviaDao rodoviaDao = new RodoviaDao(connection.getJdbcTemplate());

        Logger logger = new Logger(connection.getJdbcTemplate());
        Workbook workbook = new XSSFWorkbook("./2024.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIteratorRodovia = sheet.rowIterator();

        logger.info("Iniciando processo de ETL da artesp");

        rowIteratorRodovia.next();
        rodoviaDao.truncate();

        Integer rodoviasValidas = 0;
        Integer rodoviasNaoValidas = 0;
        Integer idUnico = 1;
        List<Rodovia> rodovias = new ArrayList<>();

        while (rowIteratorRodovia.hasNext()) {
            Row row = rowIteratorRodovia.next();

            // unicos 2 dados que não são nulos

            if(
                    row.getCell(2) != null && row.getCell(1)!= null
            ){
                Rodovia rodovia = obterRodovia(row);
                Rodovia rodoviaEncontrada = rodoviaDao.select(rodovia);

                if (rodoviaEncontrada == null) {
                    rodovia.setIdRodovia(idUnico);
//                    rodoviaDao.save(rodovia);
                    rodovias.add(rodovia);
                    rodoviasValidas++;

                    idUnico++;
                }
            }else {
                rodoviasNaoValidas++;
            }
        }
        rodoviaDao.saveAll(rodovias);
        System.out.println(rodovias.size());
        logger.info("Rodovias cadastradas com sucesso ao todo foram " + rodoviasValidas + " cadastradas e " + rodoviasNaoValidas + " não cadastradas");

        logger.info("Inciando processo de cadastro de acidentes ");

//
//        AcidenteDao acidenteDao = new AcidenteDao(connection.getJdbcTemplate());
//
//        Iterator<Row> rowIteratorAcidente = sheet.rowIterator();
//        Integer quantidade = 0;
//        while (rowIteratorAcidente.hasNext()){
//            Row row = rowIteratorAcidente.next();!= null
//            Rodovia rodovia = obterRodovia(row);
//
//
//            for (Rodovia rodoviaAtual : rodovias) {
//                if(
//                        rodoviaAtual.getNomeRodovia().equals(rodovia.getNomeRodovia()) &&
//                        rodoviaAtual.getDenominacaoRodovia().equals(rodovia.getDenominacaoRodovia()) &&
//                        rodoviaAtual.getNomeConcessionaria().equals(rodovia.getNomeConcessionaria()) &&
//                        rodoviaAtual.getMunicipioRodovia().equals(rodovia.getMunicipioRodovia()) &&
//                        rodoviaAtual.getRegionalDer().equals(rodovia.getRegionalDer()) &&
//                        rodoviaAtual.getRegAdmMunicipio().equals(rodovia.getRegAdmMunicipio())
//                ){
//                    rodovia.setIdRodovia(rodoviaAtual.getIdRodovia());
//                    quantidade++;
//
//                    break;
//                }
//            }
//
//            System.out.println(rodovia);
//            System.out.println(rodovia.getIdRodovia());
//        }
//
//        System.out.println(quantidade);


    }

    static Rodovia obterRodovia (Row row){
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
