package br.com.moveon.services;

import br.com.moveon.daos.AcidenteDao;
import br.com.moveon.daos.ConcessionariaDao;
import br.com.moveon.daos.EntityDao;
import br.com.moveon.daos.RodoviaDao;
import br.com.moveon.entites.Acidente;
import br.com.moveon.entites.Concessionaria;
import br.com.moveon.entites.Rodovia;
import br.com.moveon.services.utils.ExcelColumnIndex;
import br.com.moveon.services.utils.SlackDefaultMessages;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.util.*;

public class ETLService extends AbstractService {
    private final SlackService slackService;
    private final List<String> fileNames;
    private final List<EntityDao> entityDaos = new ArrayList<>();

    public ETLService(List<String> fileNames, SlackService slackService) {
        this.slackService = slackService;
        this.fileNames = fileNames;

        entityDaos.add(new ConcessionariaDao());
        entityDaos.add(new RodoviaDao());
        entityDaos.add(new AcidenteDao());
    }

    public void execute() {
        logger.info("Executando ETL Da Artesp");
        try {
            HashMap<String, Integer> mapConcessionariaFk = this.processarConcessionarias();
            HashMap<Rodovia, Integer> mapRodoviaFk = this.processarRodovias(mapConcessionariaFk);

            logger.info("Iniciando processo de processar todos acidentes da base de dados");
            this.processarAcidentes(mapConcessionariaFk, mapRodoviaFk);

            logger.info("Enviando alerta para canal #alerts no slack");
            slackService.sendMessage("#moveon-alerts", SlackDefaultMessages.SUCCESS_PROCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Ops houve um erro em executar o ETL: " + e.getMessage());
            slackService.sendMessage("#moveon-alerts", SlackDefaultMessages.ERROR_PROCESS);
            System.exit(0);
        }
    }

    public HashMap<String, Integer> processarConcessionarias() {
        ConcessionariaDao concessionariaDao = this.entityDaos.stream()
                .filter(dao -> dao instanceof ConcessionariaDao)
                .map( cDao -> (ConcessionariaDao) cDao).findFirst().get();

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
            concessionariaDao.saveAll(buffer);
            logger.info("Concessionarias cadastradas: " + buffer.size());
        } catch (Exception e) {
            logger.error("Erro ao salvar concessionarias: " + e.getMessage());
            System.exit(0);
        }
        return mapConcessionariaFk;
    }

    public HashMap<Rodovia, Integer> processarRodovias(HashMap<String, Integer> mapConcessionariaFk) {
        RodoviaDao rodoviaDao = this.entityDaos.stream()
                .filter(dao -> dao instanceof RodoviaDao)
                .map( rDao -> (RodoviaDao) rDao).findFirst().get();
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
            rodoviaDao.saveAll(buffer);
            logger.info("Rodovias cadastradas: " + buffer.size());
        } catch (Exception e) {
            logger.error("Erro ao salvar rodovias: " + e.getMessage());
            System.exit(0);
        }
        return mapRodoviaFk;
    }

    public void processarAcidentes(HashMap<String, Integer> mapConcessionariaFk, HashMap<Rodovia, Integer> mapRodoviaFk) {
        AcidenteDao acidenteDao = this.entityDaos.stream()
                .filter(dao -> dao instanceof AcidenteDao)
                .map( aDao -> (AcidenteDao) aDao).findFirst().get();
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
                    acidenteDao.saveAll(buffer);
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
