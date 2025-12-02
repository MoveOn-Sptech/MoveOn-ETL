package br.com.moveon.entites.enums;

public enum Clima{
    BOA("BOA"),
    CHUVA("CHUVA"),
    NUBLADO("NUBLADO"),
    NEBLINA("NEBLINA"),
    GAROA("GAROA"),
    GRANIZO("GRANIZO"),
    NAO_INFORMADA("NÃO INFORMADA"),
    CHUVA_TORRENCIAL("CHUVA TORRENCIAL"),
    CHUVA_COM_VENTANIA("CHUVA COM VENTANIA"),
    VENTO_FORTE("VENTO FORTE");

    private final String nome;



    Clima(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public static Clima getEnumFromString(String text) {
        text = text.replaceAll(" ", "_").replaceAll("Ã", "A");

        if(text.equalsIgnoreCase("SEM_INFO/NULO/0")){
            return Clima.NAO_INFORMADA;
        }

        for (Clima clima : Clima.values()) {
            if (clima.name().equalsIgnoreCase(text)) {
                return clima;
            }
        }

        throw new IllegalArgumentException("Nenhum valor enum encontrado para: " + text);
    }


}
