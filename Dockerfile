FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY /build/libs/simbir_hospital_service-0.0.1-SNAPSHOT.jar /app/hospitalService.jar
ENTRYPOINT ["java", "-jar", "hospitalService.jar"]