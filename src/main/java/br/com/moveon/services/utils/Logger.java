package br.com.moveon.services.utils;

import br.com.moveon.connection.DatabaseConnection;
import br.com.moveon.daos.LogDao;
import br.com.moveon.entites.Log;
import br.com.moveon.entites.enums.TipoLog;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static final String versao = "2.0.0";
    private static final String titulo = """
             __  __  _____     _______ ___  _   _        _     ___   ____\s
            |  \\/  |/ _ \\ \\   / / ____/ _ \\| \\ | |      | |   / _ \\ / ___|
            | |\\/| | | | \\ \\ / /|  _|| | | |  \\| |      | |  | | | | |  _\s
            | |  | | |_| |\\ V / | |__| |_| | |\\  |      | |__| |_| | |_| |
            |_|  |_|\\___/  \\_/  |_____\\___/|_| \\_|      |_____\\___/ \\____|
            
            :: MoveOn Log ::                                      (%s)
            """.formatted(versao);

    private final LogDao logDao;

    private final List<Log> logs = new ArrayList<>();

    private final static Logger INSTANCE = new Logger();

    public Logger() {
        System.out.println(Logger.titulo);
        this.logDao = new LogDao();
    }

    public String create(
            TipoLog tipoLog,
            String descricao
    ) {

        Instant dataCriacao = Instant.now().truncatedTo(ChronoUnit.MICROS); // 6 nano segundos
        Log log = new Log(
                tipoLog, descricao, dataCriacao
        );

        this.logs.add(log);
        return log.toString();
    }

    public void info(String descricao) {
        System.out.println(create(TipoLog.INFO, descricao));
    }

    public void warn(String descricao) {
        System.out.println(create(TipoLog.WARN, descricao));
    }

    public void error(String descricao) {
        System.err.println(create(TipoLog.ERROR, descricao));
    }

    public void fatal(String descricao) {
        System.err.println(create(TipoLog.FATAL, descricao));
    }

    public void saveAllLogs() {
        this.logDao.saveAll(this.logs);
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

}
