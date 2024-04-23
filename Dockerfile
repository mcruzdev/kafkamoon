FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
VOLUME /tmp
COPY target/*.jar kafkamoon-api.jar
ENTRYPOINT ["java","-jar","/kafkamoon-api.jar"]
