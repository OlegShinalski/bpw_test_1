FROM openjdk:8-jre-slim
EXPOSE 8081
RUN mkdir /app
COPY /build/libs/*.jar /app/
ENTRYPOINT ["java", "-jar", "/app/betting-0.0.1-SNAPSHOT.jar"]
