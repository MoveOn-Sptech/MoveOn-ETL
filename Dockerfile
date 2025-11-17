FROM maven:3.9.11-amazoncorretto-21-alpine AS builder

WORKDIR /app

COPY . .

RUN mvn clean package

FROM eclipse-temurin:21-jre-alpine-3.22

COPY --from=builder /app/target/moveon-log-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]