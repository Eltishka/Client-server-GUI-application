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
        Response response = new Response(3, "Коллекция очищена");
        response.setResponseCode(3);
        return response;
    }

    @Override
    public String getHelp() {
        return "Очищает коллекцию";
    }
}
