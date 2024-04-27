package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.utilities.VehicleOwnerPair;

import java.util.TreeSet;
/**
 * Реализация команды add_if_min
 * @author Piromant
 */
public class AddIfMin extends ElementCommand implements CommandUsingElement{

    public <T extends Vehicle> AddIfMin(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }


    /**
     * Метод, добавляющий элемент в коллекцию в случае, если его сила двигателя меньше, чем у любого элемента коллекции, и выводящий результат (добавлен или не добавлен)
     */
    @Override
    public Response execute() {
        TreeSet<VehicleOwnerPair> sortedCollection = new TreeSet<>(storage.getCollection());
        if(this.storage.size() == 0 || sortedCollection.first().compareTo(el) > 0)
            return (new Add(this.storage, this.argument, this.el, this.userName)).execute();
        else
            return new Response("Элемент не был добавлен");
    }

    @Override
    public String getHelp() {
        return "Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}
