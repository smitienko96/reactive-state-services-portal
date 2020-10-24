FROM wurstmeister/kafka
LABEL maintainer="Dmitry Steklyannikov \"quanizator@gmail.com\""
COPY libs/confluent-metrics-5.0.1.jar /opt/kafka/libs/confluent-metrics-5.0.1.jar
ENV KAFKA_METRIC_REPORTERS="io.confluent.metrics.reporter.ConfluentMetricsReporter"