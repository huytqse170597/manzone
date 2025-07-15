# Stage 1: Build
FROM maven:3.9.10-sapmachine-24 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:24-jdk

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 3001
ENTRYPOINT ["java", "-jar", "app.jar"]
