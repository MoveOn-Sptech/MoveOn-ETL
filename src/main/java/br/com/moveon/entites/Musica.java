package br.com.moveon.entites;

import java.time.LocalDate;

public class Musica {
    private Double id;
    private String titulo;
    private LocalDate dataLancamento;

    public Musica() {
    }

    public Musica(Double id, String titulo, LocalDate dataLancamento) {
        this.id = id;
        this.titulo = titulo;
        this.dataLancamento = dataLancamento;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "Musica{" +
               "id=" + id +
               ", titulo='" + titulo + '\'' +
               ", dataLancamento=" + dataLancamento +
               '}';
    }
}
