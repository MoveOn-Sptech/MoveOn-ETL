package br.com.moveon.services;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.daos.AcidenteDao;
import br.com.moveon.daos.ConcessionariaDao;
import br.com.moveon.daos.RodoviaDao;
import br.com.moveon.entites.Acidente;
import br.com.moveon.entites.Concessionaria;
import br.com.moveon.entites.Rodovia;
import br.com.moveon.providers.Logger;
import br.com.moveon.services.utils.ExcelColumnIndex;
import br.com.moveon.services.utils.SlackDefaultMessages;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ETLService {
    private DatabaseConnection connection;
    private final Logger logger;
    private final SlackService slackService;
    private Workbook workbook;


    public ETLService(Logger logger, DatabaseConnection connection, SlackService slackService) {
        this.logger = logger;
        this.connection = connection;
        this.slackService = slackService;

        try {
            this.workbook = new XSSFWorkbook(System.getenv("AWS_BUCKET_KEY_OBJECT"));
//            this.workbook = new XSSFWorkbook("example-error.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ops houve um erro: " + e.getMessage());
            slackService.sendMessageToChannel("#moveon-alerts", SlackDefaultMessages.ERROR_PROCESS);
            System.exit(0);
        }

    }

    public void execute() {
        try {
            HashMap<String, Integer> mapConcessionariaFk = this.extractAndSaveConcessionarias();
            HashMap<Rodovia, Integer> mapRodoviaFk = this.extractAndSaveRodovias(mapConcessionariaFk);
            this.extractAndSaveAcidentes(mapConcessionariaFk, mapRodoviaFk);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Ops hove um erro em executar o etl da aplicação: " + e.getMessage());
            System.exit(0);
        }
    }

    public HashMap<String, Integer> extractAndSaveConcessionarias() {
        Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
        iterator.next();

        HashMap<String, Integer> mapConcessionariaFk = new HashMap<>();
        List<Concessionaria> concessionarias = new ArrayList<>();

        while (iterator.hasNext()) {
            Row row = iterator.next();
            boolean naoExisteConcessionaria = mapConcessionariaFk.get(row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString()) == null;

            if (naoExisteConcessionaria) {
                Concessionaria concessionaria = new Concessionaria(concessionarias.size() + 1, row);
                mapConcessionariaFk.put(concessionaria.getNomeConcessionaria(), concessionaria.getIdConcessionaria());
                concessionarias.add(concessionaria);
            }
        }

        try {
            ConcessionariaDao concessionariaDao = new ConcessionariaDao(connection.getJdbcTemplate());
            concessionariaDao.truncate();

            concessionariaDao.saveAll(concessionarias, connection);
            logger.info("Concessionarias cadastradas com sucesso ao todo foram " + concessionarias.size());

        } catch (Exception e) {
            logger.error("Não foi possivel salvar as Concessionarias da base de dados");
            System.exit(0);
        }

        return mapConcessionariaFk;
    }

    public HashMap<Rodovia, Integer> extractAndSaveRodovias(HashMap<String, Integer> mapConcessionariaFk) {
        Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
        iterator.next();

        HashMap<Rodovia, Integer> mapRodoviaFk = new HashMap<>();
        List<Rodovia> rodovias = new ArrayList<>();


        while (iterator.hasNext()) {
            Row row = iterator.next();

//            PRIORIDADE NOME DA CONCESSONARIA E DA RODOVIA
            if (Rodovia.validaParaSalvar(row)) {
                Integer fkConcessionaria = mapConcessionariaFk.get(row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString());
                Rodovia rodovia = new Rodovia(row, fkConcessionaria);
                boolean naoExisteRodovia = mapRodoviaFk.get(rodovia) == null;

                if (naoExisteRodovia) {
                    Integer idRodovia = rodovias.size() + 1;

                    mapRodoviaFk.put(rodovia, idRodovia);
                    rodovia.setIdRodovia(idRodovia);
                    rodovias.add(rodovia);
                }
            }
        }

        try {
            RodoviaDao rodoviaDao = new RodoviaDao();

            rodoviaDao.saveAll(rodovias, connection);
            logger.info("Rodovias cadastradas com sucesso ao todo foram " + rodovias.size());

        } catch (Exception e) {
            logger.error("Não foi possivel salvar as rodovias da base de dados");
            System.exit(0);
        }

        return mapRodoviaFk;
    }

    public void extractAndSaveAcidentes(HashMap<String, Integer> mapConcessionariaFk, HashMap<Rodovia, Integer> mapRodoviaFk) {
        Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
        iterator.next();

        List<Acidente> acidentes = new ArrayList<>();

        while (iterator.hasNext()) {
            Row row = iterator.next();
            Rodovia rodovia = new Rodovia(row, mapConcessionariaFk.get(row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString()));

            rodovia.setIdRodovia(mapRodoviaFk.get(rodovia));

            if (Acidente.validoParaSalvar(row)) {
                if (row.getRowNum() % 10000 == 0) {
                    logger.info("Lendo da linha " + (row.getRowNum() - 10000) + " Até " + row.getRowNum());
                } else if (row.getRowNum() == workbook.getSheetAt(0).getLastRowNum()) {
                    logger.info("Lendo da linha " + ((row.getRowNum() / 10000) * 10000) + " Até " + row.getRowNum());
                }
                Acidente acidente = new Acidente(row, rodovia);
                acidentes.add(acidente);
            }
        }

        try {
            AcidenteDao acidenteDao = new AcidenteDao(this.connection.getJdbcTemplate());

            acidenteDao.saveAll(acidentes, connection);
            logger.info("Acidentes cadastradas com sucesso ao todo foram " + acidentes.size());
        } catch (Exception e) {
            logger.error("Não foi possivel salvar os acidentes da base de dados");
            System.exit(0);
        }

    }


}
