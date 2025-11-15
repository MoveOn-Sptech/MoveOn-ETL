package br.com.moveon.entites;

import br.com.moveon.entites.enums.Clima;
import br.com.moveon.entites.enums.TipoPista;
import br.com.moveon.services.utils.ExcelColumnIndex;
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
    private Clima clima;
    private String veiculosEnvolvidos;
    private Integer vitFatal;
    private Integer vitGrave;
    private Integer vitLeve;
    private TipoPista tipoPista;
    private Integer fkRodovia;

    public Acidente() {
    }

    public Acidente(Row row, Rodovia rodovia) {
        String dataString = row.getCell(ExcelColumnIndex.ACIDENTE_DATA_HORA_STRING).toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dataFormatada = LocalDateTime.parse(dataString, formatter);

        this.idAcidente = (int) row.getCell(ExcelColumnIndex.ACIDENTE_ID).getNumericCellValue();
        this.marcoKm = row.getCell(ExcelColumnIndex.ACIDENTE_MARCO_KM).getNumericCellValue();

        this.dtHoraAcidente = dataFormatada;

        this.tipoAcidente = row.getCell(ExcelColumnIndex.ACIDENTE_TIPO).toString();
        this.causaAcidente = row.getCell(ExcelColumnIndex.ACIDENTE_CAUSA).toString();

        this.clima = Clima.getEnumFromString(row.getCell(ExcelColumnIndex.ACIDENTE_CLIMA).toString());

        this.veiculosEnvolvidos = row.getCell(ExcelColumnIndex.ACIDENTE_VEICULOS_ENVOLVIDOS).toString();

        this.vitFatal = (int) row.getCell(ExcelColumnIndex.ACIDENTE_VIT_FATAL).getNumericCellValue();
        this.vitGrave = (int) row.getCell(ExcelColumnIndex.ACIDENTE_VIT_GRAVE).getNumericCellValue();
        this.vitLeve = (int) row.getCell(ExcelColumnIndex.ACIDENTE_VIT_LEVE).getNumericCellValue();

        this.tipoPista = TipoPista.getEnumFromString(row.getCell(ExcelColumnIndex.ACIDENTE_TIPO_PISTA).toString());

        this.fkRodovia = rodovia.getIdRodovia();
    }

    public Acidente(Integer idAcidente, Double marcoKm, LocalDateTime dtHoraAcidente, String tipoAcidente, String causaAcidente, String clima, String veiculosEnvolvidos, Integer vitFatal, Integer vitGrave, Integer vitLeve, String tipoPista, Integer fkRodovia) {
        this.idAcidente = idAcidente;
        this.marcoKm = marcoKm;
        this.dtHoraAcidente = dtHoraAcidente;
        this.tipoAcidente = tipoAcidente;
        this.causaAcidente = causaAcidente;
        this.clima = Clima.getEnumFromString(clima);
        this.veiculosEnvolvidos = veiculosEnvolvidos;
        this.vitFatal = vitFatal;
        this.vitGrave = vitGrave;
        this.vitLeve = vitLeve;
        this.tipoPista = TipoPista.getEnumFromString(tipoPista);
        this.fkRodovia = fkRodovia;
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

    public Clima getClima() {
        return clima;
    }

    public void setClima(Clima clima) {
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

    public TipoPista getTipoPista() {
        return tipoPista;
    }

    public void setTipoPista(TipoPista tipoPista) {
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


    public static Boolean validoParaSalvar(Row row) {
        return row.getCell(ExcelColumnIndex.ACIDENTE_ID) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_MARCO_KM) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_DATA_HORA_STRING) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_CAUSA) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_TIPO) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_CLIMA) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_VEICULOS_ENVOLVIDOS) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_VIT_FATAL) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_VIT_GRAVE) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_VIT_LEVE) != null &&
               row.getCell(ExcelColumnIndex.ACIDENTE_TIPO_PISTA) != null;
    }
}
