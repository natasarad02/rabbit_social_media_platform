# Koristi JDK 17 kao osnovni image
FROM eclipse-temurin:17-jdk

# Postavi radni direktorijum u kontejneru
WORKDIR /app

# Kopiraj JAR fajl u kontejner
COPY target/*.jar app.jar

# Eksponiraj port 8080
EXPOSE 8080

# Pokreni aplikaciju
ENTRYPOINT ["java", "-jar", "app.jar"]
