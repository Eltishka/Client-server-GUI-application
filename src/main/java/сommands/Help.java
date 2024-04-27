package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import сommands.authorizationscommands.AuthorizationCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * Реализация команды help
 * @author Piromant
 */
public class Help extends ElementCommand{


    private Map<String, Class<? extends Command>> commandMap;
    public <T extends Vehicle> Help(VehicleStorageManager<T> storage, String argument, T el, String userName, Map<String, Class<? extends Command>> commandMap) {
        super(storage, argument, el, userName);
        this.commandMap = commandMap;
    }

    private <T extends Vehicle> Help(VehicleStorageManager<T> storage, String argument, T el, String userName){
        super(storage, argument, el, userName);
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
            if(AuthorizationCommand.class.isAssignableFrom(command))
                continue;
            if(CommandUsingElement.class.isAssignableFrom(command))
                description += " {element, ввод элемента осущестлявется в следующих 5 строках} ";
            try {
                Constructor constructor = command.getDeclaredConstructor(VehicleStorageManager.class, String.class, Vehicle.class, String.class);
                constructor.setAccessible(true);
                description += " : " + command.getMethod("getHelp").invoke(constructor.newInstance(storage, argument, el, userName));
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
