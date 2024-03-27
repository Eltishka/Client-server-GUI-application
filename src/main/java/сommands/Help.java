package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * Реализация команды help
 * @author Piromant
 */
public class Help extends Command{


    private Map<String, Class<? extends Command>> commandMap;
    public <T extends Vehicle> Help(Storage<T> storage, String argument, T el, Map<String, Class<? extends Command>> commandMap) {
        super(storage, argument, el);
        this.commandMap = commandMap;
    }

    private <T extends Vehicle> Help(Storage<T> storage, String argument, T el){
        super(storage, argument, el);
    }
    /**
     * Метод, выводящий справку по всем командам
     */
    @Override
    public Response execute() {
        ArrayList<String> res = new ArrayList<>();
        for(String name: this.commandMap.keySet()){
            String description = name;
            Class command = this.commandMap.get(name);
            if(CommandUsingElement.class.isAssignableFrom(command))
                description += " {element, ввод элемента осущестлявется в следующих 5 строках} ";
            try {
                Constructor constructor = command.getDeclaredConstructor(Storage.class, String.class, Vehicle.class);
                constructor.setAccessible(true);
                description += " : " + command.getMethod("getHelp").invoke(constructor.newInstance(storage, argument, el));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
            res.add(description);
        }
        return new Response(res.toArray());
    }

    @Override
    public String getHelp() {
        return "Выводит справку по доступным командам";
    }
}
