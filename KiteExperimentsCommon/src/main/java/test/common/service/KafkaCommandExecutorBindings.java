package test.common.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaCommandExecutorBindings {
    String OUTGOING = "commands-outgoing";
    String INCOMING = "commands-incoming";
    String ERROR = "commands-error";

    @Output(OUTGOING)
    MessageChannel outgoing();

    @Input(INCOMING)
    SubscribableChannel incoming();

    @Input(ERROR)
    SubscribableChannel error();
}
