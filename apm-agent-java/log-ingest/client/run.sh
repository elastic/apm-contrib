#!/bin/bash

base_url=${BASE_URL:-http://localhost:8080}

doGet(){
	curl -s ${base_url}${1}
}

sleep 60

while :
do
	echo "Press [CTRL+C] to stop.."
    sleep 30
	echo "---"
    doGet /hello
	echo "---"
    doGet /errorLog
done
