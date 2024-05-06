FROM bellsoft/liberica-openjdk-alpine-musl
COPY /target/hw17-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
