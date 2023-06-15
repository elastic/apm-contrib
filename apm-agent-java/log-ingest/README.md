The goal of this subfolder is to provide end-to-end examples for java application logs ingestion.

## Requirements
- docker
- docker-compose
- an Elastic stack deployment with APM, examples are provided assuming an Elastic Cloud for simplicity.

## Configuration

The following configuration has to be provided through `.env` file in the current directory.
It will also be used with `curl` to configure ingest pipelines if needed.

```
# Filebeat configuration
# 'outpout.elasticsearch.hosts'
export elasticsearch_host=xxxxxxxx.es.yyyyyyyyyyyy.zzz.cloud.es.io
# 'cloud.id'
export cloud_id=
# 'cloud.auth'
export cloud_auth=elastic:XXXXXXXXXXXXXXXXXXXXXXXX

# APM agent configuration
export apm_server_url=

ELASTIC_APM_SECRET_TOKEN=DmuzZ2SoDz3Bx5snv6
ELASTIC_APM_SERVER_URL=https://blindpotatoes.apm.europe-west1.gcp.cloud.es.io
export apm_secret_token=
```

## Components

In addition to the Elastic stack, there are 3 main components:
- `app` : a Spring Boot application that produces some logs
- `client` : a shell script that simulates some traffic on the application
- `filebeat` : filebeat + setup ingest pipelines if needed

All of the above are deployed in separate containers, but it is not a requirement.
The `filebeat` and `app` containers use a shared volume to respectively read and write the application logs.

The ingestion strategy used here is file-based in order to make it easier to understand and debug.
That means the application writes to log files and Filebeat reads and ingest those into Elasticsearch, those log files
can thus be easily open for inspection.

In general, when using Docker or Kubernetes deployments the recommended approach is to make applications write their
logs to the standard output and use Filebeat [autodiscover](https://www.elastic.co/guide/en/beats/filebeat/current/configuration-autodiscover.html) feature to ingest this standard output into Elasticsearch, which removes the need to write to files.

The application image already contains the following to allow reusing the same image on all scenarios:
- `ecs-logging-java` as an application dependency: not used by default, allows to configure ECS logging at deployment time through re-configuration.
- Elastic Java APM Agent, the `/agent.jar` in image contains a copy of the Java agent. In the application entrypoint script, the `-javaagent:/agent.jar` parameter is added when `ELASTIC_APM_SERVICE_NAME` environment variable is set.

## Scenarios

### Base (00)

This is the base application deployment before trying to ingest logs.
Only the `app` and `client` containers are used, Filebeat and the Elastic stack is not used.
The Java APM agent is not enabled.

```
./build-and-run.sh 00
```

### Plain-text logs (01)

Application plaintext logging format is modified to include the correlation IDs.
Logging is configured by `01-app-logging.xml`.

APM agent injects the log correlation IDs at runtime.

Filebeat is configured to send the plaintext log file (see `01-filebeat.yml` for details).

```
./build-and-run.sh 01
```

### ECS Logging (02)

Application logging format is modified to use the ECS logging library.
The ECS logging library had been previously added into the application, but wasn't used.
Logging is configured by `02-app-logging.xml`.

APM agent injects the log correlation IDs at runtime (and also the `service.name` and `service.version` if they aren't provided).

Filebeat is configured to send the ECS-JSON log file (see `02-filebeat.yml` for details).

```
./build-and-run.sh 02
```

### ECS Reformatting (03)

Application logging configuration is not modified.

APM agent re-formats the log output to ECS-JSON format and injects the log correlation IDs at runtime.

Filebeat is configured to send the ECS-JSON log file (see `03-filebeat.yml` for details).

```
./build-and-run.sh 03
```

### Log sending (04)

Application and its configuration are not modified (besides the extra `-javaagent:` JVM argument and environment variables.

APM agent sends the logs directly to APM server (without filebeat) and injects the log correlation IDs at runtime.

```
./build-and-run.sh 04
```

### Otel agent (05)

Application and its configuration are not modified (besides the extra `-javaagent:` JVM argument and environment variables.

Same as 04, but with the OpenTelemetry Java agent.

```
./build-and-run.sh 05
```
