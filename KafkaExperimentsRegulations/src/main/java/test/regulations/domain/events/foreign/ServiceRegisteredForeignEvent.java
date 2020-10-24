package test.regulations.domain.events.foreign;

import test.common.domain.ForeignEvent;

public class ServiceRegisteredForeignEvent extends ForeignEvent {

    private String serviceCode;
    private String name;
    private String description;


    public ServiceRegisteredForeignEvent(long date, String uuid, String serviceId,
                                         String correlationUuid, String serviceCode, String name, String description) {
        super(date, uuid, serviceId, correlationUuid);
        this.serviceCode = serviceCode;
        this.name = name;
        this.description = description;
    }

    public String serviceCode() {
        return serviceCode;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }


}
