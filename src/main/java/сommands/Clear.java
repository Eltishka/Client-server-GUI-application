package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;

/**
 * Реализация команды clear
 * @author Piromant
 */
public class Clear extends ElementCommand{


    public <T extends Vehicle> Clear(VehicleStorageManager<T> storage, String argument, T el, String owner) {
        super(storage, argument, el, owner);
    }

    /**
     * Метод, очищающий коллекцию
     */
    @Override
    public Response execute() {
        this.storage.clear(this.userName);
        return new Response("Коллекция очищена");
    }

    @Override
    public String getHelp() {
        return "Очищает коллекцию";
    }
}
