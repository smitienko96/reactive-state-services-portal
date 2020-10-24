package test.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import test.common.configuration.EventStoreConfiguration;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.domain.PersistenceContextForeignEventHandler;
import test.common.service.DomainEventConverter;
import test.common.service.ForeignEventConverter;
import test.common.service.JooqPersistenceContextHolder;
import test.requests.service.impl.converters.ApplicationDraftHandedOverConverter;
import test.requests.service.impl.converters.ApplicationStatusChangedConverter;
import test.requests.service.impl.converters.DocumentLinkedConverter;
import test.requests.service.impl.converters.DocumentRejectedConverter;
import test.requests.service.impl.converters.foreign.DocumentCreatedConverter;
import test.requests.service.impl.eventhandlers.ApplicationDraftHandedOverJooqHandler;
import test.requests.service.impl.eventhandlers.DocumentLinkedJooqHandler;
import test.requests.service.impl.eventhandlers.foreign.DocumentCreatedJooqHandler;
import test.requests.service.impl.eventhandlers.foreign.DocumentFieldsFilledJooqHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author s.smitienko
 */
//@Configuration
public class ApplicationsEventStoreConfiguration extends EventStoreConfiguration {

    @Bean
    public Set<DomainEventConverter> domainEventConverters() {
        Set<DomainEventConverter> converters = new HashSet<>();
        converters.add(new ApplicationDraftHandedOverConverter());
        converters.add(new ApplicationStatusChangedConverter());
        converters.add(new DocumentLinkedConverter());
        converters.add(new DocumentRejectedConverter());

        return converters;
    }

    @Bean
    public Set<ForeignEventConverter> foreignEventConverters() {
        Set<ForeignEventConverter> converters = new HashSet<>();
        converters.add(new DocumentCreatedConverter());

        return converters;
    }

    @Bean
    public Set<PersistenceContextDomainEventHandler> domainEventHandlers(@Autowired JooqPersistenceContextHolder holder) {
        Set<PersistenceContextDomainEventHandler> eventHandlers = new HashSet<>();

        eventHandlers.add(new ApplicationDraftHandedOverJooqHandler(holder));
        eventHandlers.add(new DocumentLinkedJooqHandler(holder));

        return eventHandlers;
    }

    @Bean
    public Set<PersistenceContextForeignEventHandler> foreignEventHandlers(@Autowired JooqPersistenceContextHolder holder) {
        Set<PersistenceContextForeignEventHandler> eventHandlers =
                new HashSet<>();
        eventHandlers.add(new DocumentCreatedJooqHandler(holder));
        eventHandlers.add(new DocumentFieldsFilledJooqHandler(holder));

        return eventHandlers;
    }

}
