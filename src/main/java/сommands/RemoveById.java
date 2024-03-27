package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**
 *
 * Реализация команды remove_by_id
 * @author Piromant
 */
public class RemoveById extends Command implements CommandWithId{
    /**
     * id элемента, который будет удален
     */

    public <T extends Vehicle> RemoveById(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }


    /**
     * Метод, удаляющий элемент по его id и выводящий результат операци
     */
    @Override
    public Response execute() {
        boolean res = this.storage.remove(new Vehicle(Integer.parseInt(this.argument)));
        if(res)
            return new Response("Элемент удален");
        else
            return new Response("Элемента с таким id в коллекции нет");
    }

    @Override
    public String getHelp() {
        return "Удаляет элемент из коллекции по его id";
    }
}
