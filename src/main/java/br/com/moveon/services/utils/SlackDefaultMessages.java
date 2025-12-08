package br.com.moveon.services.utils;

public final class SlackDefaultMessages {

    public static final String SUCCESS_PROCESS = """
            *✅ ETL Concluído com Sucesso!* 
            O processamento de dados foi finalizado. Os novos dados já estão disponíveis. 
            _Para acesso à plataforma, acesse <http://100.30.145.237/login.html|aqui>_.
            """;

    public static final String ERROR_PROCESS = """
            *⚠️ Alerta: Falha no Processamento ETL!*
            O processo não foi concluído.
             A equipe de TI já foi notificada.
            
            _Para informações adicionais ou suporte, acesse o <https://app.slack.com/client/T09SN71JA0P/C09T3AVMEJF|aqui>_.
            """;

}
