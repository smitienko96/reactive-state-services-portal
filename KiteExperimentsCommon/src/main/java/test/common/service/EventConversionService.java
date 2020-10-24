package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.common.domain.AggregateCreatedEvent;
import test.common.domain.AggregateDeletedEvent;
import test.common.domain.DomainEvent;
import test.common.domain.Event;

import java.util.Set;


/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class EventConversionService {

    @Autowired(required = false)
    private Set<DomainEventConverter> domainConverters;

    @Autowired(required = false)
    private Set<ForeignEventConverter> foreignConverters;


    /**
     * @param avroRecord
     * @param <E>
     * @param <R>
     * @return
     * @throws EventConversionException
     */
    public <E extends Event, R extends SpecificRecord> E fromAvroRecord(R avroRecord) throws EventConversionException {

        FromAvroConverter<R, E> converter = getFromConverter(avroRecord, domainConverters);

        if (converter == null) {
            converter = getFromConverter(avroRecord, foreignConverters);
        }

        if (converter == null) {
            log.error("No converter found for " +
                            "AvroRecord Type [{}]",
                    avroRecord.getClass().getCanonicalName());
            throw new EventConversionException(avroRecord.getClass(), false);
        }

        return converter.fromAvro(avroRecord);
    }

    private <E extends Event, R extends SpecificRecord, C extends FromAvroConverter<R, E>>
    C getFromConverter(R avroRecord, Set<C> converters) {
        if (converters == null) {
            return null;
        }
        return converters.stream().filter(r -> r.recordType().equals(avroRecord.getClass()))
                .findFirst().orElse(null);
    }

    /**
     * @param event
     * @param <R>
     * @param <E>
     * @return
     * @throws EventConversionException
     */
    public <R extends SpecificRecord, E extends Event> R toAvroRecord(E event) throws EventConversionException {
        if (!isDomainEvent(event)) {
            throw new IllegalArgumentException("Conversion of foreign events " +
                    "to avro records is not supported");
        }

        ToAvroConverter<E, R> converter = null;
        if (domainConverters != null) {

            converter =
                    domainConverters.stream()
                            .filter(c -> c.objectType().equals(event.getClass()))
                            .findFirst()
                            .orElse(null);
        }

        if (converter == null) {
            log.error("No converter found for " +
                            "DomainEvent Type [%s]",
                    event.getClass().getCanonicalName());
            throw new EventConversionException(event.getClass());
        }

        return converter.toAvro(event);
    }

    /**
     *
     * @param record
     * @return
     */
    public boolean isEventRecord(SpecificRecord record) {
        if (isDomainEventRecord(record)) {
            return true;
        }
        if (foreignConverters == null) {
            return false;
        }
        return foreignConverters.stream().anyMatch(c -> c.recordType().equals(record.getClass()));
    }
    /**
     * @param event
     * @return
     */
    public boolean isDomainEvent(Event event) {
        return event instanceof DomainEvent;
    }

    /**
     * Проверяет, является ли событие начальным событием, порождающий агрегат к жизни.
     *
     * @param event событие предметной области
     * @return является ли событие начальным
     */
    public boolean isInitialDomainEvent(DomainEvent event) {
        return (event instanceof AggregateCreatedEvent);
    }

    /**
     * Проверяет, является ли событие затеняющим событием, выключающий агрегат из жизни.
     *
     * @param event событие предметной области
     * @return
     */
    public boolean isShadowingDomainEvent(DomainEvent event) {
        return (event instanceof AggregateDeletedEvent);
    }

    /**
     * @param record
     * @return
     */
    public boolean isDomainEventRecord(SpecificRecord record) {
        return domainConverters.stream().anyMatch(c -> c.recordType().equals(record.getClass()));
    }

}
