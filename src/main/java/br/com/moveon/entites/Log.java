package br.com.moveon.entites;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Log {
    private Long id;
    private String typeLog;
    private String description;
    private Instant createdAt;

    public Log() {
    }

    public Log(String typeLog, String description, Instant createdAt) {
        this.id = null;
        this.description = description;
        this.typeLog = typeLog;
        this.createdAt = createdAt;
    }

        public Log(long id, String typeLog, String description, Instant createdAt) {
        this.id = id;
        this.description = description;
        this.typeLog = typeLog;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeLog() {
        return typeLog;
    }

    public void setTypeLog(String typeLog) {
        this.typeLog = typeLog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";

        String ANSI_COLOR = switch (this.typeLog) {
            case "WARN" -> ANSI_YELLOW;
            case "ERROR", "FATAL" -> ANSI_RED;
            default -> ANSI_GREEN;
        };


        String templateLog = "%s %s --- [moveon] : %s";
        return templateLog.formatted(this.createdAt, ANSI_COLOR.concat(this.typeLog).concat(ANSI_RESET), this.description);

    }
}
