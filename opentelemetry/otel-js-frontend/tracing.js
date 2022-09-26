"use strict";

const { LogLevel } = require("@opentelemetry/core");
const { NodeTracerProvider } = require("@opentelemetry/node");
const { SimpleSpanProcessor } = require("@opentelemetry/tracing");
const { CollectorTraceExporter } = require('@opentelemetry/exporter-collector');

const provider = new NodeTracerProvider({ logLevel: LogLevel.ERROR });

provider.addSpanProcessor(
  new SimpleSpanProcessor(
    new CollectorTraceExporter({
      serviceName: "frontend"
    })
  )
);

provider.register();
console.log("tracing initialized");
