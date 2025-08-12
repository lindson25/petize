FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/petize.jar petize.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "petize.jar"]
