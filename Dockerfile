FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests
FROM openjdk:17
COPY --from=builder /app/target/*.jar task-manager.jar
ENTRYPOINT ["java", "-jar", "task-manager.jar"]
