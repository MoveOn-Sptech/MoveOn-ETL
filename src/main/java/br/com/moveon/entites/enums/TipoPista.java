package br.com.moveon.entites.enums;

public enum TipoPista {
    EIXO("EIXO"),
    MARGINAL("MARGINAL"),
    ACESSO("ACESSO"),
    INTERLIGACAO("INTERLIGAÇÃO"),
    DISPOSITIVO("DISPOSITIVO"),
    VICINAL("VICINAL"),
    NAO_INFORMADA("NÃO INFORMADA");

    private final String nome;

    TipoPista(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }


    public static TipoPista getEnumFromString(String text) {
        text = text.replaceAll(" ", "_").replaceAll("Ç", "C").replaceAll("Ã", "A");

        if(text.equalsIgnoreCase("SEM_INFO/NULO/0")){
            return TipoPista.NAO_INFORMADA;
        }

        for (TipoPista tipoPista : TipoPista.values()) {
            if (tipoPista.name().equalsIgnoreCase(text)) {
                return tipoPista;
            }
        }

        throw new IllegalArgumentException("Nenhum valor enum encontrado para: " + text);
    }

}
