package br.com.moveon.entites;

import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;

public class Rodovia {

    private Integer idRodovia;
    private String nomeRodovia;
    private String denominacaoRodovia;
    private String nomeConcessionaria;
    private String municipioRodovia;
    private String regionalDer;
    private String regAdmMunicipio;

    public Rodovia() {
    }

    public Rodovia(Row row) {
        this.nomeRodovia = row.getCell(2).toString(); // nomeRodovia
        this.denominacaoRodovia = row.getCell(20) != null ? row.getCell(20).toString() : ""; //denominacaoRodovia
        this.nomeConcessionaria = row.getCell(1).toString(); //nomeConcessionaria
        this.municipioRodovia = row.getCell(21) != null ? row.getCell(21).toString() : ""; //municipioRodovia
        this.regionalDer = row.getCell(22) != null ? row.getCell(22).toString() : "";//regionalDer
        this.regAdmMunicipio = row.getCell(22) != null ? row.getCell(23).toString() : "";//regAdmMunicipio
    }

    public Rodovia(String nomeRodovia, String denominacaoRodovia, String nomeConcessionaria, String municipioRodovia, String regionalDer, String regAdmMunicipio) {
        this.idRodovia = null;
        this.nomeRodovia = nomeRodovia;
        this.denominacaoRodovia = denominacaoRodovia;
        this.nomeConcessionaria = nomeConcessionaria;
        this.municipioRodovia = municipioRodovia;
        this.regionalDer = regionalDer;
        this.regAdmMunicipio = regAdmMunicipio;
    }

    public Integer getIdRodovia() {
        return idRodovia;
    }

    public void setIdRodovia(Integer idRodovia) {
        this.idRodovia = idRodovia;
    }

    public String getNomeRodovia() {
        return nomeRodovia;
    }

    public void setNomeRodovia(String nomeRodovia) {
        this.nomeRodovia = nomeRodovia;
    }

    public String getDenominacaoRodovia() {
        return denominacaoRodovia;
    }

    public void setDenominacaoRodovia(String denominacaoRodovia) {
        this.denominacaoRodovia = denominacaoRodovia;
    }

    public String getNomeConcessionaria() {
        return nomeConcessionaria;
    }

    public void setNomeConcessionaria(String nomeConcessionaria) {
        this.nomeConcessionaria = nomeConcessionaria;
    }

    public String getMunicipioRodovia() {
        return municipioRodovia;
    }

    public void setMunicipioRodovia(String municipioRodovia) {
        this.municipioRodovia = municipioRodovia;
    }

    public String getRegionalDer() {
        return regionalDer;
    }

    public void setRegionalDer(String regionalDer) {
        this.regionalDer = regionalDer;
    }

    public String getRegAdmMunicipio() {
        return regAdmMunicipio;
    }

    public void setRegAdmMunicipio(String regAdmMunicipio) {
        this.regAdmMunicipio = regAdmMunicipio;
    }

}
