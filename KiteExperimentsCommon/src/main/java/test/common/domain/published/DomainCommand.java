package test.common.domain.published;

/**
 * @author s.smitienko
 */
public interface DomainCommand {

    /**
     * Возвращает простое имя команды.
     *
     * @return
     */
    default String commandName() {
        return getClass().getSimpleName();
    }
}
