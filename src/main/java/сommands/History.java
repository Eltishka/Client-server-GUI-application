package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.utilities.Pair;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;
/**
 * 
 * Реализация команды history
 * @author Piromant
 */
public class History extends ElementCommand{
    /**
     * История команд в виде пар (Имя, Объект класса команды)
     */
    private Deque<Pair<String, Command>> history;

    public <T extends Vehicle> History(VehicleStorageManager<T> storage, String argument, T el, String userName,Deque<Pair<String, Command>> history) {
        super(storage, argument, el, userName);
        this.history = history;
    }

    private <T extends Vehicle> History(VehicleStorageManager<T> storage, String argument, T el, String userName){
        super(storage, argument, el, userName);
    }
    /**
     * Метод, выводящий последние 7 команд
     */
    @Override
    public Response execute() {
        return new Response(history.stream().map(Pair::getFirst).collect(Collectors.toCollection(LinkedList::new)).toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит последние 7 команд (без их аргументов)";
    }
}
