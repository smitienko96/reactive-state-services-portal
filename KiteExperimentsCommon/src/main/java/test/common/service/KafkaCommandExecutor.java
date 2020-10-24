package test.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import test.common.domain.published.DomainCommand;

@EnableBinding(KafkaCommandExecutorBindings.class)
@Slf4j
@Component
public class KafkaCommandExecutor extends CommandExecutor {

    @Autowired
    private KafkaCommandExecutorBindings binding;

    @Override
    public <R, C extends DomainCommand> Mono<R> execute(C command) {
        return getCommandHandler(command).flatMap(h -> {
            if (!(h instanceof NonReturningCommandHandler)) {
                return super.execute(command);
            }
            return sendCommandToKafka(command);
        });
    }

    @StreamListener(KafkaCommandExecutorBindings.INCOMING)
    public void processCommand(DomainCommand command) {
        log.info("Processing domain command [{}]", command);
        super.execute(command).log().block();
    }

    private <R, C extends DomainCommand> Mono<R> sendCommandToKafka(C command) {
        Message commandMessage = MessageBuilder.withPayload(command).build();
        return (Mono<R>) Mono.just(binding.outgoing().send(commandMessage)).then();
    }
}
