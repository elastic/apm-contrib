#!/bin/env bash

javaagent_opt=''
if [[ '' != "${OTEL_EXPORTER_OTLP_ENDPOINT}" ]]; then
    # using opentelemetry java agent
    javaagent_opt='-javaagent:/otel-agent.jar'
else
    if [[ '' != "${ELASTIC_APM_SERVICE_NAME}" ]]; then
        # using elastic java agent
        javaagent_opt='-javaagent:/agent.jar'
    fi
fi

cmd="java ${javaagent_opt:-} -jar /app.jar"
echo "starting app with command: '${cmd}'"

${cmd}
