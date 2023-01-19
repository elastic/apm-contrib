#!/bin/bash
id=${1:-00}

docker-compose -f ./${id}-compose.yml build
docker-compose -f ./${id}-compose.yml up
docker-compose -f ./${id}-compose.yml down
