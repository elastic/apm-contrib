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

## Scenarios

### Base

This is the base application deployment before trying to ingest logs.
Only the `app` and `client` containers are used, Filebeat and the Elastic stack is not used.

```
docker-compose -f ./00-base.yml up
```

### Plain-text logs

Application logging format is modified to include the correlation IDs
APM agent is 
```
./01-plaintext-filebeat.yml 
```