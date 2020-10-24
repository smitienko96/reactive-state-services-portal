package test.common.service;

import org.apache.avro.specific.SpecificRecord;
import test.common.domain.Event;

/**
 *
 */
public class EventConversionException extends Exception {

    public enum ConversionType {
        DOMAIN, FOREIGN, RECORD;
    }

    private Class<? extends SpecificRecord> recordClass;
    private Class<? extends Event> eventClass;
    private ConversionType conversionType;

    public EventConversionException(Class<? extends SpecificRecord> recordClass, boolean domain) {
        super(String.format("Error occurred while converting specific record of class [%s] to %s event",
                recordClass.getSimpleName(), domain ? "domain" : "foreign"));
        this.recordClass = recordClass;
        this.conversionType = domain ? ConversionType.DOMAIN : ConversionType.FOREIGN;
    }

    public EventConversionException(Class<? extends Event> eventClass) {
        super(String.format("Error occurred while converting domain event [%s] to specific record", eventClass.getSimpleName()));
        this.eventClass = eventClass;
        this.conversionType = ConversionType.RECORD;
    }

    /**
     *
     * @return
     */
    public Class<? extends SpecificRecord> recordClass() { return recordClass; }

    /**
     *
     * @return
     */
    public Class<? extends Event> eventClass() { return eventClass; }

    /**
     *
     * @return
     */
    public ConversionType conversionType() { return conversionType; }

}
