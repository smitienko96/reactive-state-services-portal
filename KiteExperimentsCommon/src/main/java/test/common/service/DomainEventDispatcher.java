package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import test.common.domain.DomainEvent;
import test.common.domain.ForeignEvent;
import test.common.domain.PersistenceContextDomainEventHandler;
import test.common.domain.PersistenceContextForeignEventHandler;

import java.util.*;

/**
 * @author s.smitienko
 */
@Component
@Slf4j
public class DomainEventDispatcher {

    private Map<Class<? extends DomainEvent>, List<PersistenceContextDomainEventHandler>> persistenceContextDomainEventHandlers;
    private Map<Class<? extends ForeignEvent>, List<PersistenceContextForeignEventHandler>> persistenceContextForeignEventHandlers;

    @EventListener
    public void handlePersistence(DomainEvent event) {
        if (persistenceContextDomainEventHandlers == null) {
            return;
        }

        List<PersistenceContextDomainEventHandler> handlers = persistenceContextDomainEventHandlers.get(event.getClass());
        if (CollectionUtils.isEmpty(handlers)) {
            log.info("No persistence event handlers configured for " +
                    "application to handle event {}", event.getClass().getCanonicalName());
            return;
        }

        handlers.forEach(handler -> handleEvent(event, handler));
    }

    @EventListener
    public void handlePersistence(ForeignEvent event) {
        if (persistenceContextForeignEventHandlers == null) {
            return;
        }

        List<PersistenceContextForeignEventHandler> handlers = persistenceContextForeignEventHandlers.get(event.getClass());
        if (CollectionUtils.isEmpty(handlers)) {
            log.info("No persistence event handlers configured for " +
                    "application to handle event {}", event.getClass().getCanonicalName());
            return;
        }

        handlers.forEach(handler -> handleEvent(event, handler));
    }

    private void handleEvent(DomainEvent event, PersistenceContextDomainEventHandler handler) {
        try {
            handler.handle(event);
        } catch (Throwable ex) {
            log.error("Exception occurred while handling domain event [{}] with handler [{}]", event, handler.getClass().getSimpleName(), ex);
        }
    }

    private void handleEvent(ForeignEvent event, PersistenceContextForeignEventHandler handler) {
        try {
            handler.handle(event);
        } catch (Throwable ex) {
            log.error("Exception occurred while handling foreign event [{}] with handler [{}]", event, handler.getClass().getSimpleName(), ex);
        }
    }

    @EventListener
    public void handleLogging(DomainEvent event) {
        log.info("Domain event occurred {}", event);
    }

    @Autowired(required = false)
    public void setPersistenceContextDomainEventHandlers(Set<PersistenceContextDomainEventHandler> eventHandlers) {
        if (CollectionUtils.isEmpty(eventHandlers)) {
            return;
        }

        this.persistenceContextDomainEventHandlers = new HashMap<>();
        eventHandlers.forEach(handler -> {
            Class<? extends DomainEvent> eventClass = handler.getEventClass();
            List<PersistenceContextDomainEventHandler> handlers = persistenceContextDomainEventHandlers.get(eventClass);
            if (handlers == null) {
                handlers = new ArrayList<>();
                this.persistenceContextDomainEventHandlers.put(eventClass, handlers);
            }
            handlers.add(handler);
        });
    }

    @Autowired(required = false)
    public void setPersistenceContextForeinEventHandlers(Set<PersistenceContextForeignEventHandler> eventHandlers) {
        if (CollectionUtils.isEmpty(eventHandlers)) {
            return;
        }

        this.persistenceContextForeignEventHandlers = new HashMap<>();
        eventHandlers.forEach(handler -> {
            Class<? extends ForeignEvent> eventClass = handler.getEventClass();
            List<PersistenceContextForeignEventHandler> handlers = persistenceContextForeignEventHandlers.get(eventClass);
            if (handlers == null) {
                handlers = new ArrayList<>();
                this.persistenceContextForeignEventHandlers.put(eventClass, handlers);
            }
            handlers.add(handler);
        });
    }
}
