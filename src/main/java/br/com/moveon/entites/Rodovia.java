package br.com.moveon.entites;

import br.com.moveon.services.utils.ExcelColumnIndex;
import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;

public class Rodovia {

    private Integer idRodovia;
    private String nome;
    private String denominacao;
    private String municipio;
    private String regionalDer;
    private String regionalAdmSp;
    private Integer fkConcessionaria;


    public Rodovia() {
    }

    public Rodovia(Row row) {
        this.nome = row.getCell(ExcelColumnIndex.RODOVIA_NOME).toString(); // nome
        this.denominacao = row.getCell(ExcelColumnIndex.RODOVIA_DENOMINACAO) != null ? row.getCell(ExcelColumnIndex.RODOVIA_DENOMINACAO).toString() : ""; //denominacao
        this.municipio = row.getCell(ExcelColumnIndex.RODOVIA_MUNICIPIO) != null ? row.getCell(ExcelColumnIndex.RODOVIA_MUNICIPIO).toString() : ""; //municipio
        this.regionalDer = row.getCell(ExcelColumnIndex.RODOVIA_REGIONAL_DER) != null ? row.getCell(ExcelColumnIndex.RODOVIA_REGIONAL_DER).toString() : "";//regionalDer
        this.regionalAdmSp = row.getCell(ExcelColumnIndex.RODOVIA_REGIONAL_ADM_SP) != null ? row.getCell(ExcelColumnIndex.RODOVIA_REGIONAL_ADM_SP).toString() : "";
    }

    public Rodovia(Row row, Integer fkConcessionaria) {
        this(row);
        this.fkConcessionaria = fkConcessionaria;
    }


    public Integer getIdRodovia() {
        return idRodovia;
    }

    public void setIdRodovia(Integer idRodovia) {
        this.idRodovia = idRodovia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(String denominacao) {
        this.denominacao = denominacao;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getRegionalDer() {
        return regionalDer;
    }

    public void setRegionalDer(String regionalDer) {
        this.regionalDer = regionalDer;
    }

    public String getRegionalAdmSp() {
        return regionalAdmSp;
    }

    public void setRegionalAdmSp(String regionalAdmSp) {
        this.regionalAdmSp = regionalAdmSp;
    }

    public Integer getFkConcessionaria() {
        return fkConcessionaria;
    }

    public void setFkConcessionaria(Integer fkConcessionaria) {
        this.fkConcessionaria = fkConcessionaria;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Rodovia rodovia = (Rodovia) object;
        return Objects.equals(nome, rodovia.nome) && Objects.equals(fkConcessionaria, rodovia.fkConcessionaria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, fkConcessionaria);
    }


    public static Boolean validaParaSalvar(Row row) {
        return row.getCell(ExcelColumnIndex.RODOVIA_NOME) != null &&
                row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME) != null;
    }

}
