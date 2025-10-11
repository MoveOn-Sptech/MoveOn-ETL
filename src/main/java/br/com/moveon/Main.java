package br.com.moveon;

import br.com.moveon.entites.Log;

import java.time.Instant;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = new Logger();

//        logger.info("dados coletado com sucesso");
        logger.warn("mensagem de alerta");

        logger.info("Usuário 'Joao' acessou a aplicação.");

        logger.info("Página inicial carregada para o usuário 'Joao'.");

        logger.warn("Usuário 'Joao' tentou acessar a página de administração sem permissão.");

        logger.warn("Tentativa invalida, usuario: joão não autorizado.");

        logger.warn("Tentativa invalida, usuario: joão não autorizado.");

        logger.warn("Tentativa invalida, usuario: joão não autorizado.");

        logger.fatal("Usuário 'Joao' bloqueado após múltiplas tentativas inválidas de acesso.");

        logger.info("Usuário 'Joao' foi desconectado da aplicação.");
    }

}




