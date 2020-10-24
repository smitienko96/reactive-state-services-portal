package test.common.service;

import test.common.domain.AggregateIdentifier;

import java.util.UUID;

/**
 * @author s.smitienko
 */
public abstract class UUIDIdentifierGenerationStrategy<I extends AggregateIdentifier> implements AggregateIdentifierGenerationStrategy<I> {

    @Override
    public final I generate() {
        return createIdentifier(generateUuid());
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public abstract I createIdentifier(UUID uuid);
}
