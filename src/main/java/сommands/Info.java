package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Реализация команды info
 * @author Piromant
 */
public class Info extends ElementCommand{

    public <T extends Vehicle> Info(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }

    /**
     * Метод, выводящий типа, дату создания и количество элементов коллекци
     */
    @Override
    public Response execute() {
        List<String> response = new LinkedList<>();
        response.add("Тип коллекции " + storage.getClass());
        response.add("Количество элементов " + storage.size());
        return new Response(response.toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит в стандартный поток вывода информацию о коллекции (тип, количество элементов)";
    }
}
