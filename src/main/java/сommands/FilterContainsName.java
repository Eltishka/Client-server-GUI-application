package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

import java.util.stream.Collectors;
/**
 * 
 * Реализация команды filter_contains_name
 * @author Piromant
 */
public class FilterContainsName extends Command{
    /**
     * @see Storage
     */
    private Storage<? extends Vehicle> storage;

    public <T extends Vehicle> FilterContainsName(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, выводящий все элементы коллекции, имена которых содержат заданную строку pattern
     */
    @Override
    public Response execute() {
        Storage<? extends Vehicle> res = this.storage.stream().filter(el -> el.getName()
                .contains(this.argument)).collect(Collectors.toCollection(Storage::new));
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
