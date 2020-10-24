package test.common.service;

import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Утилитарные методы, помогающие реализовать паттерн circuit breaker.
 *
 * @author s.smitienko
 */
public class CircuitBreakerHelper {

    private CircuitBreakerHelper() {
    }

    /**
     * Оборачивает паблишера Flux в команду Hystrix, добавляет логгирование и точку фиксации.
     *
     * @param unwrapped
     * @param commandName
     * @param fallbackFlux
     * @param <T>
     * @return
     */
    public static <T> Flux<T> wrapWithHystrix(Flux<T> unwrapped, String commandName, Flux<T> fallbackFlux) {
        return HystrixCommands.from(unwrapped)
                .commandName(commandName)
                .fallback(fallbackFlux)
                .toFlux()
                .checkpoint(commandName)
                .log();
    }

    /**
     * Оборачивает паблишера Mono в команду Hystrix, добавляет логгирование и точку фиксации.
     *
     * @param unwrapped
     * @param commandName
     * @param fallbackMono
     * @param <T>
     * @return
     */
    public static <T> Mono<T> wrapWithHystrix(Mono<T> unwrapped, String commandName, Mono<T> fallbackMono) {
        return HystrixCommands.from(unwrapped)
                .commandName(commandName)
                .commandProperties(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(30000)
                        .withCircuitBreakerErrorThresholdPercentage(10))
                .fallback(fallbackMono)
                .toMono()
                .checkpoint(commandName)
                .log();
    }
}
