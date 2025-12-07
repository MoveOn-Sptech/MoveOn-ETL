# 🚀 MoveOn ETL

Projeto acadêmico **MoveOn** responsável pelo processo **ETL (Extract, Transform, Load)**: extrair dados de fontes diversas, transformá-los e carregá-los no banco de dados.

## 🔧 Tecnologias
- Java  
- MySQL  
- JDBC  
- Maven + Docker  

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
