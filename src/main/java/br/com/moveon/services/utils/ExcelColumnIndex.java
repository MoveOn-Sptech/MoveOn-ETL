package br.com.moveon.services.utils;

public final class ExcelColumnIndex {
    // --- Colunas de Mapeamento Geral ---
    public static final int CONCESSIONARIA_NOME = 1;
    public static final int RODOVIA_NOME = 2;

    // --- Colunas para a Entidade Rodovia (Novas Adicionadas) ---
    public static final int RODOVIA_DENOMINACAO = 20;
    public static final int RODOVIA_MUNICIPIO = 21;
    public static final int RODOVIA_REGIONAL_DER = 22;

    // --- Colunas para a Entidade Acidente ---
    public static final int ACIDENTE_ID = 0;
    public static final int ACIDENTE_MARCO_KM = 3;
    public static final int ACIDENTE_DATA_HORA_STRING = 5;
    public static final int ACIDENTE_CAUSA = 6;
    public static final int ACIDENTE_TIPO = 7;
    public static final int ACIDENTE_CLIMA = 8;
    public static final int ACIDENTE_VEICULOS_ENVOLVIDOS = 10;
    public static final int ACIDENTE_VIT_FATAL = 14;
    public static final int ACIDENTE_VIT_GRAVE = 15;
    public static final int ACIDENTE_VIT_LEVE = 16;
    public static final int ACIDENTE_TIPO_PISTA = 19;
    private ExcelColumnIndex() {
    }
}