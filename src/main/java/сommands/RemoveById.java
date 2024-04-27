package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.database.UserPermissionException;

/**
 *
 * Реализация команды remove_by_id
 * @author Piromant
 */
public class RemoveById extends ElementCommand implements CommandWithId{
    /**
     * id элемента, который будет удален
     */

    public <T extends Vehicle> RemoveById(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }


    /**
     * Метод, удаляющий элемент по его id и выводящий результат операци
     */
    @Override
    public Response execute() {
        boolean res = false;
        try {
            res = this.storage.remove(new Vehicle(Integer.parseInt(this.argument)), userName);
        } catch (UserPermissionException e){
            return new Response("Недостаточно прав для удаления элемента");
        }
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
