package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**
 * 
 * Реализация "неизветсной" команды, то есть той, которой нет в списке команд
 * @author Piromant
 */
public class UnknownCommand extends Command {

    /**
     * Имя несуществующей команды
     */
    private String command;

    public <T extends Vehicle> UnknownCommand(Storage<T> storage, String argument, T el, String command) {
        super(storage, argument, el);
        this.command = command;
    }

    /**
     * Метод, выводящий информацию о несуществовании команды
     */
    @Override
    public Response execute() {
        return new Response("команды \"" + this.command + "\" нет, чтобы вывести список комманд используйте help");
    }

    @Override
    public String getHelp() {
        return null;
    }
}
