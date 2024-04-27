package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.utilities.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 
 * Реализация команды filter_contains_name
 * @author Piromant
 */
public class FilterContainsName extends ElementCommand{
    /**
     * @see VehicleStorageManager
     */

    public <T extends Vehicle> FilterContainsName(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }

    /**
     * Метод, выводящий все элементы коллекции, имена которых содержат заданную строку pattern
     */
    @Override
    public Response execute() {
        List<? extends Pair<Vehicle, String>> res = ((Collection<? extends Pair<Vehicle, String>>)this.storage.getCollection())
                .stream()
                .filter(el -> el.getFirst().getName().contains(this.argument)).
                collect(Collectors.toList());
        if(res.size() == 0){
            return new Response("Совпадений не обнаружено");
        }
        return new Response(res.toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит элементы, значение поля name которых содержит заданную подстроку";
    }

}
