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
     * Метод, обновляющий элемент в коллекции по его id и выводящий результат
     */
    @Override
    public Response execute() {
        this.el.setId(Integer.parseInt(argument));

        try {
            if(this.storage.update(el, userName)) {
                Response response = new Response("Элемент обновлен");
                response.setResponseCode(5);
                return response;
            } else {
                return new Response("Элемента с таким id в коллекции нет");
            }
        } catch (UserPermissionException e) {
            Response response =  new Response("Недостаточно прав для изменения элемента");
            response.setResponseCode(10);
            return response;
        }
    }

    @Override
    public String getHelp() {
        return "Обновляет значение элемента коллекции, id которого равен заданному";
    }
}
