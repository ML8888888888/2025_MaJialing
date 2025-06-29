FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/coin-api-1.0-SNAPSHOT.jar app.jar
COPY config.yml .
EXPOSE 8080 8081
ENTRYPOINT ["java", "-jar", "app.jar", "server", "config.yml"]