package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**
 * 
 * Реализация команды exit
 * @author Piromant
 */
public class Exit extends Command{
    public <T extends Vehicle> Exit(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, завершающий работу программы без сохранения коллекции
     */
    @Override
    public Response execute() {
        (new Save(storage, argument, el)).execute();
        return new Response("До свидания!");
    }

    @Override
    public String getHelp() {
        return "Завершает программу (без сохранения в файл)";
    }
}
