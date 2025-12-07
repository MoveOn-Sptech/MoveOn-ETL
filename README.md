# 🚀 MoveOn ETL

Projeto acadêmico **MoveOn** responsável pelo processo **ETL (Extract, Transform, Load)**: extrair dados de fontes diversas, transformá-los e carregá-los no banco de dados.

![Diagrama de Classes](https://github.com/MoveOn-Sptech/MoveOn-ETL/blob/main/src/main/resources/diagrama-de-classes.png)

## 🔧 Tecnologias

- **Java 21** → linguagem principal do projeto  
- **Maven** → gerenciamento de dependências e build (com *maven-shade-plugin*)  
- **Docker** → empacotamento e execução em container  
- **MySQL + Driver** → persistência e integração com banco de dados  
- **Spring JDBC** → acesso e manipulação de dados  
- **Jackson Databind** → serialização/deserialização JSON  
- **Apache POI (poi-ooxml)** → leitura e escrita de arquivos Excel  
- **AWS SDK S3** → integração com armazenamento em nuvem (Amazon S3)

## ▶️ Execução

### Local
```bash
mvn clean package
java -jar target/moveon-log-1.0-SNAPSHOT.jar
```

### Docker
```bash
docker build -t moveon-etl .
docker run --rm moveon-etl
```

## 👥 Contribuição
Faça um fork, crie uma branch, implemente melhorias e abra um pull request.
