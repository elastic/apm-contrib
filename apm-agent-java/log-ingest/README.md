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

## Scenarios

### Base (00)

This is the base application deployment before trying to ingest logs.
Only the `app` and `client` containers are used, Filebeat and the Elastic stack is not used.

```
docker-compose -f ./00-compose.yml up
docker-compose -f ./00-compose.yml down
```

### Plain-text logs (01)

Application plaintext logging format is modified to include the correlation IDs.

APM agent injects the log correlation IDs at runtime.

Filebeat is configured to send the plaintext log file (see `01-filebeat.yml` for details).

```
docker-compose -f ./01-compose.yml up
docker-compose -f ./01-compose.yml down
```
### ECS Reformatting (02)

Application logging configuration is not modified.

APM agent reformats the log output to ECS-JSON format and injects the log correlation IDs at runtime.

Filebeat is configured to send the ECS-JSON log file (see `02-filebeat.yml` for details).

```
docker-compose -f ./02-compose.yml up
docker-compose -f ./02-compose.yml down
```
