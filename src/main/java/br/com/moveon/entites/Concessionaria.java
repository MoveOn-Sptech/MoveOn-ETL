package br.com.moveon.entites;

import br.com.moveon.services.utils.ExcelColumnIndex;
import org.apache.poi.ss.usermodel.Row;
import java.util.Objects;

public class Concessionaria {

    private  Integer idConcessionaria;

    private String nomeConcessionaria;

    public Concessionaria() {
    }

    public Concessionaria(Integer idConcessionaria, Row row) {
        this.idConcessionaria = idConcessionaria;
        this.nomeConcessionaria = row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME).toString();
    }

    public Concessionaria(Integer idConcessionaria, String nomeConcessionaria) {
        this.idConcessionaria = idConcessionaria;
        this.nomeConcessionaria = nomeConcessionaria;
    }

    public Integer getIdConcessionaria() {
        return idConcessionaria;
    }

    public void setIdConcessionaria(Integer idConcessionaria) {
        this.idConcessionaria = idConcessionaria;
    }

    public String getNomeConcessionaria() {
        return nomeConcessionaria;
    }

    public void setNomeConcessionaria(String nomeConcessionaria) {
        this.nomeConcessionaria = nomeConcessionaria;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Concessionaria that = (Concessionaria) object;
        return Objects.equals(idConcessionaria, that.idConcessionaria) && Objects.equals(nomeConcessionaria, that.nomeConcessionaria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idConcessionaria, nomeConcessionaria);
    }

}
