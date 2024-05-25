package client;
/**
 *
 * Функциональный интерфейс для проверки аргументов
 * @author Piromant
 */
@FunctionalInterface
public interface ArgumentChecker {
    public boolean check(String arg);
}
