package test.common.service;

import reactor.core.publisher.Mono;
import test.common.domain.published.DomainCommand;

/**
 * @author s.smitienko
 */
public interface CommandHandler<C extends DomainCommand, R> {
    Mono<R> handle(C command);

    Class<C> getCommandClass();
}
