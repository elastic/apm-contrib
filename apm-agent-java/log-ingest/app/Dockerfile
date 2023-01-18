# app build
FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -q -f /home/app/pom.xml clean package

# package
FROM openjdk:17-ea-jdk
COPY --from=build /home/app/target/app.jar /app.jar
COPY --from=docker.elastic.co/observability/apm-agent-java:latest /usr/agent/elastic-apm-agent.jar /agent.jar
COPY ./run.sh /run.sh
RUN chmod +x /run.sh

# temp hack to get latest snapshot agent
RUN curl -o '/agent.jar' -L 'https://apm-ci.elastic.co/job/apm-agent-java/job/apm-agent-java-mbp/job/main/lastSuccessfulBuild/artifact/src/github.com/elastic/apm-agent-java/elastic-apm-agent/target/elastic-apm-agent-1.35.1-SNAPSHOT.jar'

EXPOSE 8080
ENTRYPOINT ["/run.sh"]
