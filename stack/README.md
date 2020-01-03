## Stack Quickstart

This folder contains a minimal `docker-compose.yml` for quickly bringing up an Elastic APM stack.
Get started by downloading it and bringing up a few containers:

```sh
curl -sLO https://raw.githubusercontent.com/elastic/apm-contrib/master/stack/docker-compose.yml
docker-compose up
```

This will download docker container images for Elasticsearch, Kibana, and APM Server and start each service.

Follow steps 4 & 5 on http://localhost:5601/app/kibana#/home/tutorial/apm to configure your application to collect and report APM data.

Visit http://localhost:5601/app/apm to see the results!

*to stop*: hit ctrl-c to shut down all of the containers started

## Updating

This `docker-compose.yml` was generated from the [apm-integration-testing repository](https://github.com/elastic/apm-integration-testing) with:

```sh
./scripts/compose.py start 7.5.1 --release --docker-compose-path - | sed 's/7.5.1/${STACK_VERSION:-7.5.1}/'
```
