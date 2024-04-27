package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.utilities.VehicleOwnerPair;
import server.utilities.Pair;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
/**
 * 
 * Реализация команды print_field_descending_engine_power
 * @author Piromant
 */
public class PrintFieldDescendingEnginePower extends ElementCommand{

    public <T extends Vehicle> PrintFieldDescendingEnginePower(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }


    /**
     * Метод, выводящий все элементы коллекции в порядке убывания их силы двигателя
     */
    @Override
    public Response execute() {
        if(this.storage.size() == 0){
            return new Response("Коллекция пуста");
        }
        return new Response(((Collection<VehicleOwnerPair<Vehicle, String>>) (this.storage.getCollection())).stream().sorted(Comparator.reverseOrder()).distinct().toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит значения поля enginePower всех элементов в порядке убывания";
    }
}
