package br.com.moveon.entites;

import br.com.moveon.services.utils.ExcelColumnIndex;
import org.apache.poi.ss.usermodel.Row;
import java.util.Objects;

public class Concessionaria {

    private  Integer idConcessionaria;

    private String nome;

    public Concessionaria() {
    }

    public Concessionaria(Integer idConcessionaria, Row row) {
        this.idConcessionaria = idConcessionaria;
        this.nome = row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString();
    }

    public Concessionaria(Integer idConcessionaria, String nome) {
        this.idConcessionaria = idConcessionaria;
        this.nome = nome;
    }

    public Integer getIdConcessionaria() {
        return idConcessionaria;
    }

    public void setIdConcessionaria(Integer idConcessionaria) {
        this.idConcessionaria = idConcessionaria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Concessionaria that = (Concessionaria) object;
        return Objects.equals(idConcessionaria, that.idConcessionaria) && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idConcessionaria, nome);
    }

}
