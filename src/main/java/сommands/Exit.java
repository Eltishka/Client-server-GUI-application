package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;

/**
 * 
 * Реализация команды exit
 * @author Piromant
 */
public class Exit extends ElementCommand{
    public <T extends Vehicle> Exit(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }

    /**
     * Метод, завершающий работу программы без сохранения коллекции
     */
    @Override
    public Response execute() {
        return new Response("До свидания!");
    }

    @Override
    public String getHelp() {
        return "Завершает программу (без сохранения в файл)";
    }
}
