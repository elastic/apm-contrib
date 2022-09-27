# Copyright The OpenTelemetry Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import logging
from flask import Flask, request

from opentelemetry import propagators, trace
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import OTLPSpanExporter
from opentelemetry.instrumentation.flask import FlaskInstrumentor
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import SimpleSpanProcessor

logging.basicConfig(level=logging.DEBUG)

app = Flask(__name__)

FlaskInstrumentor().instrument_app(app)

tracer_provider = TracerProvider(resource=Resource.create({"service.name": "backend"}))
tracer_provider.add_span_processor(
    SimpleSpanProcessor(OTLPSpanExporter())
)
trace.set_tracer_provider(tracer_provider)


@app.route("/backend")
def server_request():
    return "served"


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8081)
