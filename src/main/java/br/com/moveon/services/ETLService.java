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
            logger.info("Iniciando processo de processar todos acidentes da base de dados");
            this.processarBaseDeDados();

            logger.info("Enviando alerta para canal #alerts no slack");
            slackService.sendMessage("#moveon-alerts", SlackDefaultMessages.SUCCESS_PROCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Ops houve um erro em executar o ETL: " + e.getMessage());
            slackService.sendMessage("#moveon-alerts", SlackDefaultMessages.ERROR_PROCESS);
            System.exit(0);
        }
    }

    public void processarBaseDeDados() {
        ConcessionariaDao concessionariaDao = this.entityDaos.stream().filter(dao -> dao instanceof ConcessionariaDao).map(dao -> (ConcessionariaDao) dao).findFirst().get();
        RodoviaDao rodoviaDao = this.entityDaos.stream().filter(dao -> dao instanceof RodoviaDao).map(dao -> (RodoviaDao) dao).findFirst().get();
        AcidenteDao acidenteDao = this.entityDaos.stream().filter(dao -> dao instanceof AcidenteDao).map(dao -> (AcidenteDao) dao).findFirst().get();

        concessionariaDao.truncate();

        HashMap<String, Integer> mapConcessionariaFk = new HashMap<>();
        HashMap<Rodovia, Integer> mapRodoviaFk = new HashMap<>();

        List<Concessionaria> bufferConcessionarias = new ArrayList<>();
        List<Rodovia> bufferRodovias = new ArrayList<>();
        List<Acidente> bufferAcidentes = new ArrayList<>();

        try {
            for (String fileName : fileNames) {
                logger.info("Lendo o Arquivo: " + fileName);

                try (Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName))) {
                    Iterator<Row> iterator = workbook.getSheetAt(0).rowIterator();
                    if (iterator.hasNext()) iterator.next(); // pula header

                    while (iterator.hasNext()) {
                        Row row = iterator.next();

                        // Concessionária
                        String nomeConcessionaria = row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString();
                        if (!mapConcessionariaFk.containsKey(nomeConcessionaria)) {
                            Concessionaria concessionaria = new Concessionaria(mapConcessionariaFk.size() + 1, row);
                            mapConcessionariaFk.put(concessionaria.getNome(), concessionaria.getIdConcessionaria());
                            bufferConcessionarias.add(concessionaria);
                        }

                        // Rodovia
                        if (Rodovia.validaParaSalvar(row)) {
                            Integer fkConcessionaria = mapConcessionariaFk.get(nomeConcessionaria);
                            Rodovia rodovia = new Rodovia(row, fkConcessionaria);
                            if (!mapRodoviaFk.containsKey(rodovia)) {
                                Integer idRodovia = mapRodoviaFk.size() + 1;
                                rodovia.setIdRodovia(idRodovia);
                                mapRodoviaFk.put(rodovia, idRodovia);
                                bufferRodovias.add(rodovia);
                            }
                        }

                        // Acidente
                        if (Acidente.validoParaSalvar(row)) {
                            Rodovia rodovia = new Rodovia(row, mapConcessionariaFk.get(nomeConcessionaria));
                            rodovia.setIdRodovia(mapRodoviaFk.get(rodovia));
                            Acidente acidente = new Acidente(row, rodovia);
                            bufferAcidentes.add(acidente);
                        }
                    }
                }
            }

            logger.info("Iniciando processamento em lote da captura dos dados");
            // Salvar em lote
            concessionariaDao.saveAll(bufferConcessionarias);
            logger.info("Concessionarias Salvas: " + bufferConcessionarias.size());

            rodoviaDao.saveAll(bufferRodovias);
            logger.info("Rodovias Salvas: " + bufferRodovias.size());

            acidenteDao.saveAll(bufferAcidentes);
            logger.info("Acidentes Salvas: " + bufferAcidentes.size());
        } catch (Exception e) {
            logger.error("Erro ao processar ETL: " + e.getMessage());
            System.exit(0);
        }
    }
}
