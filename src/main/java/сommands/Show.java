package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;

/**
 * 
 * Реализация команды show
 * @author Piromant
 */
public class Show extends ElementCommand{

    public <T extends Vehicle> Show(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }


    /**
     * Метод, выводящий все элементы коллекции в порядке их добавления
     */
    @Override
    public Response execute() {
        if(storage.size() > 0)
            return new Response(storage.getCollection().stream().sorted().toArray());
        else
            return new Response("В коллекции нет элементов");
    }

    @Override
    public String getHelp() {
        return "Выводит  в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
