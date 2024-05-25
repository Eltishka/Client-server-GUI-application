package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;


/**
 * 
 * Реализация команды add
 * @author Piromant
 */
public class Add extends ElementCommand implements CommandUsingElement{

    public <T extends Vehicle> Add(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }

    /**
     * Метод, добавляющий элемент в коллекцию и выводящий результат (добавлен или не добавлен)
     */
    @Override
    public Response execute() {
        if(this.storage.add(el, userName)) {
            Response response = new Response("Элемент добавлен");
            response.setResponseCode(1);
            return response;
        }
        else {
            Response response = new Response("Элемент не был добавлен");
            response.setResponseCode(2);
            return response;
        }
    }

    @Override
    public String getHelp() {
        return "добавляет новый элемент в коллекцию, ввод элемента осущестлявется в следующих 5 строках";
    }
}
