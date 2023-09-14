FROM openjdk:17
WORKDIR /app
COPY target/challenge-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","challenge-0.0.1-SNAPSHOT.jar"]