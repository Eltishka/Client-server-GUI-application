package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**
 * 
 * Реализация команды update
 * @author Piromant
 */
public class Update extends Command implements CommandUsingElement, CommandWithId{

    public <T extends Vehicle> Update(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, обнавляющий элемент в коллекции по его id и выводящий результат
     */
    @Override
    public Response execute() {
        this.el.setId(Integer.parseInt(argument));
        boolean res = this.storage.remove(el);

        if(res) {
            this.storage.add(el);
            return new Response("Элемент обновлен");
        } else {
            return new Response("Элемента с таким id в коллекции нет");
        }
    }

    @Override
    public String getHelp() {
        return "Обновляет значение элемента коллекции, id которого равен заданному";
    }
}
