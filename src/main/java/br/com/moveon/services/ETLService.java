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
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.util.*;

public class ETLService {
    private final DatabaseConnection connection;
    private final Logger logger;
    private final SlackService slackService;
    private final List<String> fileNames;

    public ETLService(List<String> fileNames, Logger logger, DatabaseConnection connection, SlackService slackService) {
        this.logger = logger;
        this.connection = connection;
        this.slackService = slackService;
        this.fileNames = fileNames;
    }

    public void execute() {
        logger.info("Executando ETL Da Artesp");
        try {
            HashMap<String, Integer> mapConcessionariaFk = this.extractAndSaveConcessionarias();
            HashMap<Rodovia, Integer> mapRodoviaFk = this.extractAndSaveRodovias(mapConcessionariaFk);

            logger.info("Iniciando processo de processar todos acidentes da base de dados");
            this.extractAndSaveAcidentes(mapConcessionariaFk, mapRodoviaFk);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Ops houve um erro em executar o ETL: " + e.getMessage());
            System.exit(0);
        }
    }

    public HashMap<String, Integer> extractAndSaveConcessionarias() {
        ConcessionariaDao concessionariaDao = new ConcessionariaDao(connection.getJdbcTemplate());
        concessionariaDao.truncate();

        HashMap<String, Integer> mapConcessionariaFk = new HashMap<>();
        List<Concessionaria> buffer = new ArrayList<>();

        try {
            for (String fileName : fileNames) {
                try (Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))) {
                    Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
                    if (iterator.hasNext()) iterator.next(); // pula header

                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        String nome = row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString();
                        if (!mapConcessionariaFk.containsKey(nome)) {
                            Concessionaria concessionaria = new Concessionaria(mapConcessionariaFk.size() + 1, row);
                            mapConcessionariaFk.put(concessionaria.getNome(), concessionaria.getIdConcessionaria());
                            buffer.add(concessionaria);
                        }
                    }
                }
            }
            concessionariaDao.saveAll(buffer, connection);
            logger.info("Concessionarias cadastradas: " + buffer.size());
        } catch (Exception e) {
            logger.error("Erro ao salvar concessionarias: " + e.getMessage());
            System.exit(0);
        }
        return mapConcessionariaFk;
    }

    public HashMap<Rodovia, Integer> extractAndSaveRodovias(HashMap<String, Integer> mapConcessionariaFk) {
        HashMap<Rodovia, Integer> mapRodoviaFk = new HashMap<>();
        List<Rodovia> buffer = new ArrayList<>();

        try {
            for (String fileName : fileNames) {
                try (Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))) {
                    Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
                    if (iterator.hasNext()) iterator.next();

                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        if (Rodovia.validaParaSalvar(row)) {
                            Integer fkConcessionaria = mapConcessionariaFk.get(row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString());
                            Rodovia rodovia = new Rodovia(row, fkConcessionaria);
                            if (!mapRodoviaFk.containsKey(rodovia)) {
                                Integer idRodovia = mapRodoviaFk.size() + 1;
                                rodovia.setIdRodovia(idRodovia);
                                mapRodoviaFk.put(rodovia, idRodovia);
                                buffer.add(rodovia);
                            }
                        }
                    }
                }
            }
            new RodoviaDao(connection.getJdbcTemplate()).saveAll(buffer, connection);
            logger.info("Rodovias cadastradas: " + buffer.size());
        } catch (Exception e) {
            logger.error("Erro ao salvar rodovias: " + e.getMessage());
            System.exit(0);
        }
        return mapRodoviaFk;
    }

    public void extractAndSaveAcidentes(HashMap<String, Integer> mapConcessionariaFk, HashMap<Rodovia, Integer> mapRodoviaFk) {
        AcidenteDao acidenteDao = new AcidenteDao(connection.getJdbcTemplate());

        try {
            for (String fileName : fileNames) {
                logger.info("Lendo o Arquivo: " + fileName);

                try (Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))) {
                    Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
                    if (iterator.hasNext()) iterator.next();

                    List<Acidente> buffer = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        Rodovia rodovia = new Rodovia(row, mapConcessionariaFk.get(row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString()));
                        rodovia.setIdRodovia(mapRodoviaFk.get(rodovia));

                        if (Acidente.validoParaSalvar(row)) {
                            Acidente acidente = new Acidente(row, rodovia);
                            buffer.add(acidente);

                            if (row.getRowNum() % 10000 == 0) {
                                logger.info("Lendo da linha " + (row.getRowNum() - 10000) + " Até " + row.getRowNum());
                            } else if (row.getRowNum() == workbook.getSheetAt(0).getLastRowNum()) {
                                logger.info("Lendo da linha " + ((row.getRowNum() / 10000) * 10000) + " Até " + row.getRowNum());
                            }
                        }
                    }
                    acidenteDao.saveAll(buffer, connection);
                    buffer.clear();
                }
            }
            logger.info("Acidentes processados e salvos com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao salvar acidentes: " + e.getMessage());
            System.exit(0);
        }
    }
}
