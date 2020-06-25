"use strict";

const { LogLevel } = require("@opentelemetry/core");
const { NodeTracerProvider } = require("@opentelemetry/node");
const { SimpleSpanProcessor } = require("@opentelemetry/tracing");
const { CollectorExporter } = require('@opentelemetry/exporter-collector');

const provider = new NodeTracerProvider({ logLevel: LogLevel.ERROR });

provider.addSpanProcessor(
  new SimpleSpanProcessor(
    new CollectorExporter({
      serviceName: "frontend",
      url: 'opentelemetry-collector:55678'
    })
  )
);

provider.register();
console.log("tracing initialized");
