FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=demo-0.0.1-SNAPSHOT.jar

# cd /opt/app
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY ${JAR_FILE} app.jar
COPY movies.json /app/
COPY movies.yaml /app/
COPY src ./src

RUN ./mvnw dependency:resolve

ENTRYPOINT ["java","-jar","app.jar"]