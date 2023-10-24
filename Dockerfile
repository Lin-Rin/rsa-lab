#FROM eclipse-temurin:17-jdk-focal
FROM --platform=linux/arm64 eclipse-temurin:17-jdk-ubi9-minimal

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]