package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

import java.util.TreeSet;
/**
 * 
 * Реализация команды add_if_max
 * @author Piromant
 */
public class AddIfMax extends Command implements CommandUsingElement{

    public <T extends Vehicle> AddIfMax(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }


    /**
     * Метод, добавляющий элемент в коллекцию в случае, если его сила двигателя больше, чем у любого элемента коллекции, и выводящий результат (добавлен или не добавлен)
     * @return
     */
    @Override
    public Response execute() {
        TreeSet<Vehicle> sortedCollection = new TreeSet<>(storage);
        if(this.storage.size() == 0 || sortedCollection.last().compareTo(el) < 0)
            return (new Add(this.storage, this.argument, this.el)).execute();
        else
            return new Response("Элемент не был добавлен");
    }

    @Override
    public String getHelp() {
        return "Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }
}
