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
from opentelemetry.ext.wsgi import collect_request_attributes
from opentelemetry.ext.otlp.trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import SimpleExportSpanProcessor

logging.basicConfig(level=logging.DEBUG)

app = Flask(__name__)

trace.set_tracer_provider(TracerProvider(
    resource=Resource({"service.name": "backend"})
))
tracer = trace.get_tracer_provider().get_tracer(__name__)

trace.get_tracer_provider().add_span_processor(
    SimpleExportSpanProcessor(OTLPSpanExporter(endpoint="opentelemetry-collector:55680"))
)


@app.route("/backend")
def server_request():
    with tracer.start_as_current_span(
        "GET /backend",
        parent=propagators.extract(
            lambda request, key: request.headers.get_all(key), request
        )["current-span"],
        kind=trace.SpanKind.SERVER,
        attributes=collect_request_attributes(request.environ),
    ) as foo:
        return "served"


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8081)
