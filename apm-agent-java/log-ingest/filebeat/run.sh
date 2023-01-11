#!/bin/bash

# create/update our ingest pipeline
curl \
  -u "${cloud_auth}" \
  -XPUT \
  "https://${elasticsearch_host}/_ingest/pipeline/log-ingest-plaintext" \
  -H "Content-Type: application/json" \
  -d'
{
  "processors": [
    {
      "grok": {
        "field": "message",
          "patterns": [
            "%{TIMESTAMP_ISO8601:@timestamp}\\s+%{LOGLEVEL:log.level}\\s+%{NUMBER:pid} ---.* : %{GREEDYDATA:message} --- (%{WORD:trace.id})?,(%{WORD:transaction.id})?,(%{WORD:error.id})?\n\n(?m)%{GREEDYDATA:error.stack_trace}",
            "%{TIMESTAMP_ISO8601:@timestamp}\\s+%{LOGLEVEL:log.level}\\s+%{NUMBER:pid} ---.* : %{GREEDYDATA:message} --- (%{WORD:trace.id})?,(%{WORD:transaction.id})?,(%{WORD:error.id})?",
            "%{TIMESTAMP_ISO8601:@timestamp}\\s+%{LOGLEVEL:log.level}\\s+%{NUMBER:pid} ---.* : %{GREEDYDATA:message}\\n\\n(?m)%{GREEDYDATA:error.stack_trace}",
            "%{TIMESTAMP_ISO8601:@timestamp}\\s+%{LOGLEVEL:log.level}\\s+%{NUMBER:pid} ---.* : %{GREEDYDATA:message}"
        ]
      }
    }
  ]
}'

# 'strict.perms' is required in docker as config will be mounted and won't have the expected permissions

# check configuration
filebeat \
    -c filebeat.yml \
    -strict.perms=false \
    test output


# run filebeat
filebeat \
    -c filebeat.yml \
    -strict.perms=false \
    -e
