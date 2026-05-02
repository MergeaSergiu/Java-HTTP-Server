# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package dependency:copy-dependencies \
    -DoutputDirectory=target/dependency -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/target/HTTPServer-1.0-SNAPSHOT.jar app.jar
COPY --from=build /build/target/dependency ./dependency
COPY --from=build /build/src/main/resources ./src/main/resources

EXPOSE 8080
CMD ["java", "-cp", "app.jar:dependency/*", "com.server.HttpServer"]
