package test.common.service;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author s.smitienko
 */
public interface KafkaEventProcessor {

    String OUTGOING = "events-outgoing";
    String INCOMING = "events-incoming";
    String PROJECTING_OUTGOING = "events-projecting-outgoing";
    String PROJECTING_INCOMING = "events-projecting-incoming";

    String QUARANTINE_OUTGOING = "events-quarantine-outgoing";
    String DLQ_OUTGOING = "dlq";
    String ERROR_INCOMING = "events-errors-incoming";

    /**
     * Канал исходящих сообщений процессора.
     *
     * @return исходящий канал
     */
    @Output(OUTGOING)
    MessageChannel outgoing();

    /**
     * Канал (стрим) входящих сообщений процессора
     *
     * @return входящий канал
     */
    @Input(INCOMING)
    KStream<String, SpecificRecord> incoming();

    /**
     * Проекция (лог изменений), сгруппированный ко ученикам
     *
     * @return проекция
     */
    @Input(PROJECTING_INCOMING)
    KStream<String, AggregateEventsHolder> projecting();

    @Output(PROJECTING_OUTGOING)
    KStream<String, AggregateEventsHolder> projectingOutgoing();

    @Output(QUARANTINE_OUTGOING)
    MessageChannel quarantineOutgoing();
//
//    @Input(ERROR_INCOMING)
//    SubscribableChannel errorIncoming();
}
