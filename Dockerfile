#FROM eclipse-temurin:17-jdk-focal
#FROM --platform=linux/arm64/v8 eclipse-temurin:17.0.7_7-jre-jammy
FROM --platform=linux/amd64 eclipse-temurin:17.0.7_7-jre-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]