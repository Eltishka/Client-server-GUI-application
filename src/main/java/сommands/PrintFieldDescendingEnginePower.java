package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
/**
 * 
 * Реализация команды print_field_descending_engine_power
 * @author Piromant
 */
public class PrintFieldDescendingEnginePower extends Command{

    public <T extends Vehicle> PrintFieldDescendingEnginePower(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }


    /**
     * Метод, выводящий все элементы коллекции в порядке убывания их силы двигателя
     */
    @Override
    public Response execute() {
        if(this.storage.size() == 0){
            return new Response("Коллекция пуста");
        }
        LinkedHashSet<? extends Vehicle> res = ((Storage<? super Vehicle>)(this.storage)).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toCollection(LinkedHashSet::new));
        return new Response(res.toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит значения поля enginePower всех элементов в порядке убывания";
    }
}
