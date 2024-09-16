package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;

/**
 * 
 * Реализация "неизветсной" команды, то есть той, которой нет в списке команд
 * @author Piromant
 */
public class UnknownCommand extends ElementCommand {

    /**
     * Имя несуществующей команды
     */
    private String command;

    public <T extends Vehicle> UnknownCommand(VehicleStorageManager<T> storage, String argument, T el, String command, String userName) {
        super(storage, argument, el, userName);
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
