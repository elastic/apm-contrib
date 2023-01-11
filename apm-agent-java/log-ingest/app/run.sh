#!/bin/env bash

javaagent_opt=''
if [[ '' != "${ELASTIC_APM_SERVICE_NAME}" ]]; then
    javaagent_opt='-javaagent:/agent.jar'
fi

if [[ -f /env.sh ]]; then
    source /env.sh
fi

cmd="java ${javaagent_opt:-} -jar /app.jar"
echo "starting app with command: '${cmd}'"

${cmd}
