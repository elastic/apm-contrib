## Stack Quickstart

This folder contains a minmal `docker-compose.yml` for quickly bringing up an Elastic APM stack.
To get started, clone this repository and run:

```sh
docker-compose -f stack/docker-compose.yml up
```

This will download docker container images for Elasticsearch, Kibana, and APM Server and start each service.

Follow steps 4 & 5 on http://localhost:5601/app/kibana#/home/tutorial/apm to configure your application to collect and report APM data.

Visit http://localhost:5601/app/apm to see the results!

*to stop*: hit ctrl-c to shut down all of the containers started

## Updating

This `docker-compose.yml` was generated from the [apm-integration-testing repository](https://github.com/elastic/apm-integration-testing) with:

```sh
./scripts/compose.py start 7.2.0 --release --docker-compose-path -
```
