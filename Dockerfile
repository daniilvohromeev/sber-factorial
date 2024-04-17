
FROM openjdk:17-jdk


WORKDIR /app


COPY target/*.jar /app/sber-factorial.jar


EXPOSE 8099


ENTRYPOINT ["java", "-jar", "/app/sber-factorial.jar"]