package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Реализация команды info
 * @author Piromant
 */
public class Info extends Command{

    public <T extends Vehicle> Info(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, выводящий типа, дату создания и количество элементов коллекци
     */
    @Override
    public Response execute() {
        List<String> response = new LinkedList<>();
        response.add("Тип коллекции " + storage.getClass());
        response.add("Дата создания " + this.storage.getCreationDate());
        response.add("Количество элементов " + storage.size());
        return new Response(response.toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)";
    }
}
