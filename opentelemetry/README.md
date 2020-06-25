## OpenTelemetry Quickstart

This folder contains a `docker-compose.yml` for quickly bringing up an Elastic APM stack,
along with a demo application. The application is comprised of three components:

 - "pinger", which periodically sends an HTTP request to the "frontend"
 - "frontend", a Node.js (Express) web service which serves a single endpoint "/", which triggers an HTTP request to "backend"
 - "backend", a Python (Flask) web service which serves a single endpoint "/backend"

The "pinger" service is instrumented with Elastic APM, whereas "frontend" and "backend" are both instrumented with
OpenTelemetry, and configured to send their trace events to an OpenTelemetry Collector. Because both Elastic APM and
OpenTelemetry use the W3C Trace-Context standard, distributed traces are continued despite using the two methods of
instrumentation.

Get started by cloning this repo, and bringing up the stack and demo application:

```sh
git clone https://github.com/elastic/apm-contrib
cd apm-contrib/opentelemetry
docker-compose up
```

This will download Docker container images for Elasticsearch, Kibana, APM Server, and OpenTelemetry Collector, and
build the custom application containers. Once they are all running, the demo application will automatically start
producing APM data.

Visit http://localhost:5601/app/apm to see the results!

*to stop*: hit ctrl-c to shut down all of the containers started
