FROM eclipse-temurin:17-jre-focal
ARG JAR_FILE=target/Virtualknowledge-0.0.1.jar
COPY target/Virtualknowledge-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]