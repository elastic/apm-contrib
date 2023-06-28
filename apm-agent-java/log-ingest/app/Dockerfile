# app build
FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -q -f /home/app/pom.xml clean package


# package
FROM openjdk:17-ea-jdk
COPY --from=build /home/app/target/app.jar /app.jar
COPY --from=docker.elastic.co/observability/apm-agent-java:latest /usr/agent/elastic-apm-agent.jar /agent.jar
RUN curl -L -o /otel-agent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.27.0/opentelemetry-javaagent.jar
COPY ./run.sh /run.sh
RUN chmod +x /run.sh

EXPOSE 8080
ENTRYPOINT ["/run.sh"]
