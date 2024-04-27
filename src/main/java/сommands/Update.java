package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.database.UserPermissionException;

/**
 * 
 * Реализация команды update
 * @author Piromant
 */
public class Update extends ElementCommand implements CommandUsingElement, CommandWithId{

    public <T extends Vehicle> Update(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }

    /**
     * Метод, обнавляющий элемент в коллекции по его id и выводящий результат
     */
    @Override
    public Response execute() {
        this.el.setId(Integer.parseInt(argument));

        try {
            if(this.storage.update(el, userName)) {
                return new Response("Элемент обновлен");
            } else {
                return new Response("Элемента с таким id в коллекции нет");
            }
        } catch (UserPermissionException e) {
            return new Response("Недостаточно прав для изменения элемента");
        }
    }

    @Override
    public String getHelp() {
        return "Обновляет значение элемента коллекции, id которого равен заданному";
    }
}
