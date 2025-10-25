package br.com.moveon.providers;

import br.com.moveon.daos.LogDao;
import br.com.moveon.entites.Log;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Logger {
    String version = "2.0.0";
    String title = """
             __  __  _____     _______ ___  _   _        _     ___   ____\s
            |  \\/  |/ _ \\ \\   / / ____/ _ \\| \\ | |      | |   / _ \\ / ___|
            | |\\/| | | | \\ \\ / /|  _|| | | |  \\| |      | |  | | | | |  _\s
            | |  | | |_| |\\ V / | |__| |_| | |\\  |      | |__| |_| | |_| |
            |_|  |_|\\___/  \\_/  |_____\\___/|_| \\_|      |_____\\___/ \\____|
            
            :: MoveOn Log ::                                      (%s)
            """.formatted(version);

    //https://pt.wikipedia.org/wiki/ISO_8601#:~:text=A%20ISO%208601%20%C3%A9%20uma,tr%C3%AAs%20expans%C3%B5es%20mostradas%20s%C3%A3o%20v%C3%A1lidas.&text=O%20objetivo%20primordial%20da%20norma,padr%C3%B5es%20nacionais%20e%20lingu%C3%ADsticos%20existentes.
//    DateTimeFormatter LOG_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";

    private LogDao logDao;

    public Logger(JdbcTemplate jdbcTemplate) {
        this.init();
        this.logDao = new LogDao(jdbcTemplate);
    }

    String create(
            String typeLog,
            String description
    ) {

        String ANSI_COLOR = switch (typeLog) {
            case "WARN" -> ANSI_YELLOW;
            case "ERROR", "FATAL" -> ANSI_RED;
            default -> ANSI_GREEN;
        };

        Instant createdAtFormated = Instant.now().truncatedTo(ChronoUnit.MICROS); // 6 nano segundos
        Log log = new Log(
                typeLog, description, createdAtFormated
        );
        this.logDao.save(log);

       return log.toString();
    }

    public  void info(String description) {
        System.out.println(create("INFO", description));
    }

    public  void warn(String description) {
        System.out.println(create("WARN", description));
    }

    public  void error(String description) {
        System.err.println(create("ERROR", description));
    }

    public  void fatal(String description) {
        System.err.println(create("FATAL", description));
    }

    public  void init() {
        System.out.println(title);
    }

    public LogDao getLogDao() {
        return logDao;
    }

}
