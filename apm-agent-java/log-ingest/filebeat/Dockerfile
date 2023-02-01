ARG ELASTIC_STACK_VERSION=latest
FROM elastic/filebeat:${ELASTIC_STACK_VERSION}

USER root
COPY run.sh /run.sh
RUN chmod +x /run.sh

USER filebeat
ENTRYPOINT ["/run.sh"]
