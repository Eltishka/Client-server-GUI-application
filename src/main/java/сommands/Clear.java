package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**
 * Реализация команды clear
 * @author Piromant
 */
public class Clear extends Command{


    public <T extends Vehicle> Clear(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, очищающий коллекцию
     */
    @Override
    public Response execute() {
        this.storage.clear();
        return new Response("Коллекция очищена");
    }

    @Override
    public String getHelp() {
        return "Очищает коллекцию";
    }
}
