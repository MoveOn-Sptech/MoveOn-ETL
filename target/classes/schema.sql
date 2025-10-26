CREATE DATABASE moveon;

USER moveon;

CREATE TABLE logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50),
    descricao TEXT,
    dataCriacao TIMESTAMP(6)
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
    dtHoraAcidente DATETIME, -- Armazenando data e hora do acidente
    tipoAcidente VARCHAR(45),
    causaAcidente VARCHAR(45),
    clima VARCHAR(45),
    veiculosEnvolvidos VARCHAR(255),
    vitFatal INT,
    vitGrave INT,
    vitLeve INT,
    tipoPista VARCHAR(45),
    fkRodovia INT NOT NULL, -- Chave estrangeira para Rodovia
    PRIMARY KEY (idAcidente),
    FOREIGN KEY (fkRodovia) REFERENCES Rodovia(idRodovia)
);
