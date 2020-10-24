package test.regulations.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Component
public class RegulationsFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!exchange.getRequest().getPath().value().contains("applicant-documents")) {
            return chain.filter(exchange);
        }

        exchange.getRequest().getBody().doOnNext(buffer -> {
            InputStream stream = buffer.asInputStream();
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                do {
                    line = reader.readLine();
                    if (line != null) {
                        builder.append(line);
                    }
                } while (line != null);
            } catch (IOException ex) {
                log.error("Error occurred while reading request from stream", ex);
            }
            log.info("Body received from outside: [{}]", builder.toString());
        });
        return chain.filter(exchange);
    }
}
