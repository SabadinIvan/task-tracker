FROM amazoncorretto:17.0.17-al2023-headless
WORKDIR /app
COPY task-spring-app-0.0.1-SNAPSHOT.jar /app/task-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "task-app.jar"]