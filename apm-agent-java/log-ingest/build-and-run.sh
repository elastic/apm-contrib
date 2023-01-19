#!/bin/bash
id=${1:-00}

docker volume rm log-ingest_shared-volume-${id} 2>/dev/null
docker-compose -f ./${id}-compose.yml build
docker-compose -f ./${id}-compose.yml up
docker-compose -f ./${id}-compose.yml down
