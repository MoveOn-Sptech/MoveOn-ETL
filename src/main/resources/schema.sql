CREATE DATABASE IF NOT EXISTS moveon;
USE moveon;


CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cargo VARCHAR(45),
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(512) NOT NULL,
    dataCadastro DATETIME NOT NULL
);


CREATE TABLE Log (
    idLog INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(45) NOT NULL,
    descricao TEXT,
    dataCriacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fkUsuario INT,
    FOREIGN KEY (fkUsuario) REFERENCES Usuario(idUsuario)
);


CREATE TABLE Concessionaria (
    idConcessionaria INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL
);


CREATE TABLE Rodovia (
    idRodovia INT AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL,
    denominacao VARCHAR(45),
    municipio VARCHAR(45),
    regionalDer VARCHAR(45),
    regionalAdmSp VARCHAR(45) NOT NULL,
    fkConcessionaria INT NOT NULL,
    FOREIGN KEY (fkConcessionaria) REFERENCES Concessionaria(idConcessionaria),
    PRIMARY KEY (idRodovia, fkConcessionaria)
);


CREATE TABLE Notificacao (
    idNotificacao INT AUTO_INCREMENT,
    dataCriacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    titulo VARCHAR(45) NOT NULL,
    conteudo TEXT NOT NULL,
    fkUsuario INT NOT NULL,
    fkConcessionaria INT NOT NULL,
    FOREIGN KEY (fkUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (fkConcessionaria) REFERENCES Concessionaria(idConcessionaria),
    PRIMARY KEY (idNotificacao, fkUsuario, fkConcessionaria)
);

CREATE TABLE Acidente (
    idAcidente INT AUTO_INCREMENT,
    marcoKm DECIMAL(10, 2),
    dtHoraAcidente DATETIME,
    tipoAcidente VARCHAR(45),
    causaAcidente VARCHAR(45),
    clima VARCHAR(45),
    qtdVitFatal INT,
    qtdVitGrave INT,
    qtdVitLeve INT,
    tipoPista VARCHAR(45),
    fkRodovia INT,
    fkConcessionaria INT,
    FOREIGN KEY (fkRodovia) REFERENCES Rodovia(idRodovia),
    FOREIGN KEY (fkConcessionaria) REFERENCES Rodovia(fkConcessionaria),
    PRIMARY KEY (idAcidente, fkRodovia, fkConcessionaria)
);

CREATE TABLE Veiculo (
    idVeiculo INT AUTO_INCREMENT,
    tipo VARCHAR(45),
    quantidade INT,
    fkAcidente INT,
    fkRodovia INT,
    fkConcessionaria INT,
    FOREIGN KEY (fkAcidente) REFERENCES Acidente(idAcidente),
    FOREIGN KEY (fkRodovia) REFERENCES Acidente(fkRodovia),
    FOREIGN KEY (fkConcessionaria) REFERENCES Acidente(fkConcessionaria),
    PRIMARY KEY(idVeiculo, fkAcidente, fkRodovia, fkConcessionaria)
);