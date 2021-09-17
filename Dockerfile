FROM openjdk:11-jre-slim

ARG JAR_FILE=build/libs/FT-1.0.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-XX:+UseSerialGC","-Xss512k","-XX:MaxRAM=256m","/app.jar"]
