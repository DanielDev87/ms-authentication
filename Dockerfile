# --- Etapa 1: Build ---
FROM openjdk:21-jdk-slim AS builder
WORKDIR /app

# Copiamos solo los archivos necesarios para Gradle para aprovechar el cache de Docker.
# Esto asegura que si solo cambian los sources, no se invalide esta capa.
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY main.gradle .
COPY applications/ applications/
COPY domain/ domain/
COPY infrastructure/ infrastructure/

# Damos permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# *** NUEVA LÍNEA: Limpiar el caché de Gradle en el contenedor ***
# Esto borrará el caché de dependencias y de la construcción anterior.
RUN rm -rf ~/.gradle/caches && ./gradlew --stop && ./gradlew clean

# Construimos el JAR, apuntando al subproyecto 'app-service'.
RUN ./gradlew :applications:app-service:bootJar -x test

# --- Etapa 2: Ejecución (Runtime) ---
FROM openjdk:21-slim
WORKDIR /app
COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]