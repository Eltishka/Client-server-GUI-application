package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

import java.util.TreeSet;
/**
 * Реализация команды add_if_min
 * @author Piromant
 */
public class AddIfMin extends Command implements CommandUsingElement{

    public <T extends Vehicle> AddIfMin(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }


    /**
     * Метод, добавляющий элемент в коллекцию в случае, если его сила двигателя меньше, чем у любого элемента коллекции, и выводящий результат (добавлен или не добавлен)
     */
    @Override
    public Response execute() {
        TreeSet<Vehicle> sortedCollection = new TreeSet<>(storage);
        if(this.storage.size() == 0 || sortedCollection.first().compareTo(el) > 0)
            return (new Add(this.storage, this.argument, this.el)).execute();
        else
            return new Response("Элемент не был добавлен");
    }

    @Override
    public String getHelp() {
        return "Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}
