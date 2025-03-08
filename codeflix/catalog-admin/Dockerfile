FROM eclipse-temurin:21-alpine

COPY build/libs/*.jar opt/app/application.jar

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

CMD java -jar /opt/app/application.jar
