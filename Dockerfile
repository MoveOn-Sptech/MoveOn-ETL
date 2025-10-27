FROM maven:3.9.11-amazoncorretto-21-alpine

COPY . .

RUN mvn clean package

ENTRYPOINT ["java", "-jar", "target/moveon-log-1.0-SNAPSHOT.jar"]