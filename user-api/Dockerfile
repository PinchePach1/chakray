FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY target/user-api-0.0.1-SNAPSHOT.jar user-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-api.jar"]