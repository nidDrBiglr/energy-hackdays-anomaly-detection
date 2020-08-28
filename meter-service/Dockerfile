FROM openjdk:14-alpine

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

EXPOSE 8080
EXPOSE 8880

ENTRYPOINT ["java","-Dspring.devtools.restart.enabled=false", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
