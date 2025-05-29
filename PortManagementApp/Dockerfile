FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/PortManagementApp-0.0.1-SNAPSHOT.jar app.jar

# Uruchom aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]
