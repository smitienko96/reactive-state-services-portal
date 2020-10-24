FROM docker.elastic.co/logstash/logstash:6.5.0
LABEL maintainer="Dmitry Steklyannikov \"quanizator@gmail.com\""
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
ADD pipeline/ /usr/share/logstash/pipeline/
