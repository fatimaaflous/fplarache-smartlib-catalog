FROM openjdk:17
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expose le port 8081, sur lequel l'application va tourner
EXPOSE 8081