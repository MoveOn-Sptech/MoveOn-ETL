package br.com.moveon.entites;

import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Acidente {
    private Integer idAcidente;
    private Double marcoKm;
    private LocalDateTime dtHoraAcidente;
    private String tipoAcidente;
    private String causaAcidente;
    private String clima;
    private String veiculosEnvolvidos;
    private Integer vitFatal;
    private Integer vitGrave;
    private Integer vitLeve;
    private String tipoPista;
    private Integer fkRodovia;

    public Acidente() {
    }

    public Acidente(Row row, Rodovia rodovia) {
        String dataString = row.getCell(5).toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dataFormatada = LocalDateTime.parse(dataString, formatter);

        this.idAcidente = (int) row.getCell(0).getNumericCellValue();
        this.marcoKm = row.getCell(3).getNumericCellValue();
        this.dtHoraAcidente = dataFormatada;
        this.tipoAcidente = row.getCell(7).toString();
        this.causaAcidente = row.getCell(6).toString();
        this.clima = row.getCell(8).toString();
        this.veiculosEnvolvidos = row.getCell(10).toString();
        this.vitFatal = (int) row.getCell(14).getNumericCellValue();
        this.vitGrave = (int) row.getCell(15).getNumericCellValue();
        this.vitLeve = (int) row.getCell(16).getNumericCellValue();
        this.tipoPista = row.getCell(19).toString();
        this.fkRodovia = rodovia.getIdRodovia();
    }

    public Acidente(Integer idAcidente, Double marcoKm, LocalDateTime dtHoraAcidente, String tipoAcidente, String causaAcidente, String clima, String veiculosEnvolvidos, Integer vitFatal, Integer vitGrave, Integer vitLeve, String tipoPista, Integer fkRodovia) {
        this.idAcidente = idAcidente;
        this.marcoKm = marcoKm;
        this.dtHoraAcidente = dtHoraAcidente;
        this.tipoAcidente = tipoAcidente;
        this.causaAcidente = causaAcidente;
        this.clima = clima;
        this.veiculosEnvolvidos = veiculosEnvolvidos;
        this.vitFatal = vitFatal;
        this.vitGrave = vitGrave;
        this.vitLeve = vitLeve;
        this.tipoPista = tipoPista;
        this.fkRodovia = fkRodovia;
    }

    public Acidente(Row row) {

    }



    public Integer getIdAcidente() {
        return idAcidente;
    }

    public void setIdAcidente(Integer idAcidente) {
        this.idAcidente = idAcidente;
    }

    public Double getMarcoKm() {
        return marcoKm;
    }

    public void setMarcoKm(Double marcoKm) {
        this.marcoKm = marcoKm;
    }

    public LocalDateTime getDtHoraAcidente() {
        return dtHoraAcidente;
    }

    public void setDtHoraAcidente(LocalDateTime dtHoraAcidente) {
        this.dtHoraAcidente = dtHoraAcidente;
    }

    public String getTipoAcidente() {
        return tipoAcidente;
    }

    public void setTipoAcidente(String tipoAcidente) {
        this.tipoAcidente = tipoAcidente;
    }

    public String getCausaAcidente() {
        return causaAcidente;
    }

    public void setCausaAcidente(String causaAcidente) {
        this.causaAcidente = causaAcidente;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getVeiculosEnvolvidos() {
        return veiculosEnvolvidos;
    }

    public void setVeiculosEnvolvidos(String veiculosEnvolvidos) {
        this.veiculosEnvolvidos = veiculosEnvolvidos;
    }

    public Integer getVitFatal() {
        return vitFatal;
    }

    public void setVitFatal(Integer vitFatal) {
        this.vitFatal = vitFatal;
    }

    public Integer getVitGrave() {
        return vitGrave;
    }

    public void setVitGrave(Integer vitGrave) {
        this.vitGrave = vitGrave;
    }

    public Integer getVitLeve() {
        return vitLeve;
    }

    public void setVitLeve(Integer vitLeve) {
        this.vitLeve = vitLeve;
    }

    public String getTipoPista() {
        return tipoPista;
    }

    public void setTipoPista(String tipoPista) {
        this.tipoPista = tipoPista;
    }

    public Integer getFkRodovia() {
        return fkRodovia;
    }

    public void setFkRodovia(Integer fkRodovia) {
        this.fkRodovia = fkRodovia;
    }

    @Override
    public String toString() {
        return "Acidente{" +
               "idAcidente=" + idAcidente +
               ", marcoKm=" + marcoKm +
               ", dtHoraAcidente=" + dtHoraAcidente +
               ", tipoAcidente='" + tipoAcidente + '\'' +
               ", causaAcidente='" + causaAcidente + '\'' +
               ", clima='" + clima + '\'' +
               ", veiculosEnvolvidos='" + veiculosEnvolvidos + '\'' +
               ", vitFatal=" + vitFatal +
               ", vitGrave=" + vitGrave +
               ", vitLeve=" + vitLeve +
               ", tipoPista='" + tipoPista + '\'' +
               ", fkRodovia=" + fkRodovia +
               '}';
    }
}
