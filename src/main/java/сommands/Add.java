package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**
 * 
 * Реализация команды add
 * @author Piromant
 */
public class Add extends Command implements CommandUsingElement{

    public <T extends Vehicle> Add(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, добавляющий элемент в коллекцию и выводящий результат (добавлен или не добавлен)
     */
    @Override
    public Response execute() {
        if(this.storage.add(el))
            return new Response("Элемент добавлен");
        else
            return new Response("Элемент не был добавлен");
    }

    @Override
    public String getHelp() {
        return "добавляет новый элемент в коллекцию, ввод элемента осущестлявется в следующих 5 строках";
    }
}
