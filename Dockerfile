# Dockerfile for cloud deployment of the Gradle backend
FROM gradle:latest AS build
COPY --chown=gradle:gradle . /src
WORKDIR /src
COPY build.gradle /src
RUN gradle build --no-daemon --stacktrace --x test
ENV DB_URL=jdbc:mysql://host.docker.internal:3306

# Java 17 is required for the Spring Boot application
FROM openjdk:17-oracle
EXPOSE 8080
RUN mkdir /app
COPY --from=build /src/build/libs/votingapp.jar app.jar
COPY src/main/resources/logback-spring.xml logback-spring.xml
ENTRYPOINT ["java","-jar","app.jar"]