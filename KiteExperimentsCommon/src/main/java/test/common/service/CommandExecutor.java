package test.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;
import test.common.domain.published.DomainCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author s.smitienko
 */
@Component
public class CommandExecutor {

    private Map<Class<? extends DomainCommand>, CommandHandler> handlers;

    /**
     * Выполняет команду предметной области и возвращает результат.
     *
     * @param command команда предметной области
     * @param <R>
     * @param <C>
     * @return
     */
    public <R, C extends DomainCommand> Mono<R> execute(C command) {
        return getCommandHandler(command).flatMap(h -> h.handle(command));
    }

    protected <C extends DomainCommand> Mono<CommandHandler> getCommandHandler(C command) {
        CommandHandler handler =  handlers.get(command.getClass());
        if (handler == null) {
            return Mono.error(new RuntimeException(String.format("No handler " +
                    "specified for " +
                    "command [%s]", command.getClass().getSimpleName())));
        }
        return Mono.just(handler);
    }

    @Autowired
    public void setCommandHandlers(Set<CommandHandler> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            throw new RuntimeException("Command handlers couldn't be empty");
        }

        this.handlers = new HashMap<>(handlers.size());

        handlers.forEach(h -> this.handlers.put(h.getCommandClass(), h));
    }
}
