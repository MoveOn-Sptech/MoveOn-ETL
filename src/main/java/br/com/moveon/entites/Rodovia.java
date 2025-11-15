package br.com.moveon.entites;

import br.com.moveon.services.utils.ExcelColumnIndex;
import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;

public class Rodovia {

    private Integer idRodovia;
    private String nomeRodovia;
    private String denominacaoRodovia;
    private String municipioRodovia;
    private String regionalDer;
    private Integer fkConcessionaria;


    public Rodovia() {
    }

    public Rodovia(Row row) {
        this.nomeRodovia = row.getCell(ExcelColumnIndex.RODOVIA_NOME).toString(); // nomeRodovia
        this.denominacaoRodovia = row.getCell(ExcelColumnIndex.RODOVIA_DENOMINACAO) != null ? row.getCell(ExcelColumnIndex.RODOVIA_DENOMINACAO).toString() : ""; //denominacaoRodovia
        this.municipioRodovia = row.getCell(ExcelColumnIndex.RODOVIA_MUNICIPIO) != null ? row.getCell(ExcelColumnIndex.RODOVIA_MUNICIPIO).toString() : ""; //municipioRodovia
        this.regionalDer = row.getCell(ExcelColumnIndex.RODOVIA_REGIONAL_DER) != null ? row.getCell(ExcelColumnIndex.RODOVIA_REGIONAL_DER).toString() : "";//regionalDer
    }

    public Rodovia(Row row, Integer fkConcessionaria) {
        this(row);
        this.fkConcessionaria = fkConcessionaria;
    }

    public Rodovia(String nomeRodovia, String denominacaoRodovia, String nomeConcessionaria, String municipioRodovia, String regionalDer, String regAdmMunicipio) {
        this.idRodovia = null;
        this.nomeRodovia = nomeRodovia;
        this.denominacaoRodovia = denominacaoRodovia;
        this.municipioRodovia = municipioRodovia;
        this.regionalDer = regionalDer;
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
        return Objects.equals(nomeRodovia, rodovia.nomeRodovia) && Objects.equals(fkConcessionaria, rodovia.fkConcessionaria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeRodovia, fkConcessionaria);
    }


    public static Boolean validaParaSalvar(Row row) {
        return row.getCell(ExcelColumnIndex.RODOVIA_NOME) != null &&
               row.getCell(ExcelColumnIndex.CONCESSIONARIA_NOME) != null;
    }

}
