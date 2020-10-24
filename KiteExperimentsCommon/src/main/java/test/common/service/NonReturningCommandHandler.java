package test.common.service;

import test.common.domain.published.DomainCommand;

/**
 * @author s.smitienko
 */
public interface NonReturningCommandHandler<C extends DomainCommand> extends CommandHandler<C, Void> {
}
