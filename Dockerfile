# Etapa 1: Construir el JAR
FROM maven:3.8.7-eclipse-temurin-11 AS build

WORKDIR /app

# Copia el archivo pom.xml y las dependencias para el caché
COPY pom.xml .
COPY src ./src

# Construye el JAR
RUN mvn clean package -DskipTests

# Etapa 2: Construir la imagen final
FROM eclipse-temurin:11-jdk

WORKDIR /app

# Copia el JAR desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
