FROM amazoncorretto:17.0.17-al2023-headless
WORKDIR /app
COPY requestGen.jar /app/requestGen.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "requestGen.jar"]