# Etapa 1: compilar con Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Descarga las dependencias (se cachea mientras el pom no cambie)
RUN mvn -q dependency:go-offline
COPY src ./src
# Las pruebas se omiten: algunas necesitan la base de datos, que no existe al construir
RUN mvn -q package -DskipTests

# Etapa 2: ejecutar solo con el JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]