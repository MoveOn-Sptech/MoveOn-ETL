CREATE DATABASE moveon;

USER moveon;

CREATE TABLE logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
      typeLog VARCHAR(50),
    description TEXT,
    createdAt TIMESTAMP(6)
);

CREATE TABLE Rodovia (
    idRodovia INT NOT NULL PRIMARY KEY auto_increment,
    nomeRodovia VARCHAR(45),
    denominacaoRodovia VARCHAR(45),
    nomeConcessionaria VARCHAR(45),
    municipioRodovia VARCHAR(45),
    regionalDer VARCHAR(45),
    regAdmMunicipio VARCHAR(45)
);


-- Tabela Acidente
CREATE TABLE Acidente (
    idAcidente INT NOT NULL,
    marcoKm DECIMAL(10, 2), -- Ajuste o tamanho e precisão conforme necessário
    dataAcidente DATE,
    horaAcidente DATETIME, -- Armazenando data e hora do acidente
    tipoAcidente VARCHAR(45),
    causaAcidente VARCHAR(45),
    clima VARCHAR(45),
    veiculosEnvolvidos VARCHAR(45),
    vitFatal INT,
    vitGrave INT,
    vitLeve INT,
    tipoPista VARCHAR(45),
    Rodovia_idRodovia INT NOT NULL, -- Chave estrangeira para Rodovia
    PRIMARY KEY (idAcidente),
    FOREIGN KEY (Rodovia_idRodovia) REFERENCES Rodovia(idRodovia)
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