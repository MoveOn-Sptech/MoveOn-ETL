CREATE DATABASE moveon;

USER moveon;

CREATE TABLE logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
      typeLog VARCHAR(50),
    description TEXT,
    createdAt TIMESTAMP(6)
);

select * from logs;

INSERT INTO logs (typeLog, description, createdAt)
VALUES ('INFO', 'O serviço de processamento de pedidos foi inicializado.', NOW(6));

INSERT INTO logs (typeLog, description, createdAt)
VALUES ('WARN', 'A conexão com o cache externo atingiu o tempo limite.', NOW(6));

INSERT INTO logs (typeLog, description, createdAt)
VALUES ('ERROR', 'Exceção não tratada ao tentar calcular o frete.', NOW(6));

SELECT
    id,
    typeLog,
    description,
    createdAt
FROM logs
ORDER BY id DESC;