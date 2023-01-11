#!/bin/bash

base_url=${BASE_URL:-http://localhost:8080}

sleep 60

while :
do
	echo "Press [CTRL+C] to stop.."
	curl ${base_url}/hello
	echo "---"
  curl ${base_url}/errorLog
  sleep 30
done
