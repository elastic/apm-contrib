## OpenTelemetry Quickstart

This folder contains a `docker-compose.yml` for quickly bringing up an Elastic APM stack,
along with a demo application. The application is comprised of three components:

 - `pinger` which periodically sends an HTTP request to the "frontend"
 - `frontend` a Node.js (Express) web service which serves a single endpoint "/", which triggers an HTTP request to "backend"
 - `backend` a Python (Flask) web service which serves a single endpoint "/backend"

The `pinger` service is instrumented with Elastic APM, whereas `frontend` and `backend` are both instrumented with
OpenTelemetry, and configured to send their trace events to an OpenTelemetry Collector. Because both Elastic APM and
OpenTelemetry use the W3C Trace-Context standard, distributed traces are continued despite using the two methods of
instrumentation.

### Getting started
Get started by cloning this repo:

```sh
git clone https://github.com/elastic/apm-contrib
cd apm-contrib/opentelemetry
```

Then we need to create a configuration file for Elasticsearch and Kibana that will help on the creation of the TLS certificates and credentials.

```sh
echo '
# Password for the 'elastic' user (at least 6 characters)
ELASTIC_PASSWORD=elastic
KIBANA_PASSWORD=kibana

# Set the cluster name
CLUSTER_NAME=docker-cluster

# Port to expose Elasticsearch HTTP API to the host
ES_PORT=9200
#ES_PORT=127.0.0.1:9200

# Port to expose Kibana to the host
KIBANA_PORT=5601
#KIBANA_PORT=80

# Increase or decrease based on the available host memory (in bytes)
MEM_LIMIT=1073741824' > .env
```

Then bringing up the stack and demo application and wait:
```sh
docker-compose up
```


This will download Docker container images for Elasticsearch, Kibana, APM Server, and OpenTelemetry Collector, and
build the custom application containers. Once they are all running, the demo application will automatically start
producing APM data.

Visit http://127.0.0.1:5601/app/apm to see the results!

*to stop*: hit ctrl-c to shut down all of the containers started
