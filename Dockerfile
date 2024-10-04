FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY /build/libs/service.jar /app/hospitalService.jar
ENTRYPOINT ["java", "-jar", "hospitalService.jar"]