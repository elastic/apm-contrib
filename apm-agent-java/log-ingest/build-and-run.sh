#!/bin/bash
id=${1:-00}

# set compose project name to allow running them at the same time
# otherwise defauts to current working directory and only one can run at a time
export COMPOSE_PROJECT_NAME="log-ingest-${id}"

docker volume rm log-ingest_shared-volume-${id} 2>/dev/null
docker-compose -f ./${id}-compose.yml build
docker-compose -f ./${id}-compose.yml up
docker-compose -f ./${id}-compose.yml down
