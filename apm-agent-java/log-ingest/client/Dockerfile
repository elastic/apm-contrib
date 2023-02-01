FROM ubuntu:22.04
RUN apt-get update \
    && apt-get install -y curl watch

COPY run.sh /run.sh
RUN chmod +x /run.sh

ENV BASE_URL="http://localhost:8080"

ENTRYPOINT ["/run.sh"]