FROM amazoncorretto:17.0.6

COPY target/MockedService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app.jar"]