FROM eclipse-temurin:17-jre
WORKDIR /app
ADD ./target/appointment-service.jar .
ENTRYPOINT ["java","-jar","/app/appointment-service.jar"]