package br.com.moveon.entites;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Log {
    private Long id;
    private String tipo;
    private String descricao;
    private Instant dataCriacao;

    public Log() {
    }

    public Log(String tipo, String descricao, Instant dataCriacao) {
        this.id = null;
        this.descricao = descricao;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
    }

        public Log(long id, String tipo, String descricao, Instant dataCriacao) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Instant dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {

        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";

        String ANSI_COLOR = switch (this.tipo) {
            case "WARN" -> ANSI_YELLOW;
            case "ERROR", "FATAL" -> ANSI_RED;
            default -> ANSI_GREEN;
        };


        String templateLog = "%s %s --- [moveon] : %s";
        return templateLog.formatted(this.dataCriacao, ANSI_COLOR.concat(this.tipo).concat(ANSI_RESET), this.descricao);

    }
}
