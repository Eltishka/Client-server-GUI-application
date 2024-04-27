package server;

import objectspace.Vehicle;
import org.slf4j.LoggerFactory;
import server.app.authorization.UserPermission;
import server.database.UserStorageManager;
import server.database.VehicleStorageManager;
import сommands.*;
import сommands.authorizationscommands.AuthorizationCommand;


import java.util.Deque;

public class Invoker {
    private static Invoker invoker = new Invoker();

    private CommandHashMap commandMap;
    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(CommandExecuter.class);

    private Invoker(){
        this.commandMap = new CommandHashMap();
    }
    private Invoker(CommandHashMap commandMap){
        this.commandMap = commandMap;
    }

    public CommandHashMap getCommandMapClone(){
        return (CommandHashMap) commandMap.clone();
    }

    public static Invoker getAccess(){
        return new Invoker(invoker.commandMap);
    }


    public static void register(String name, Class<? extends Command> command){
        invoker.commandMap.put(name, command);

    }

    public <T extends Vehicle> Command getCommandToExecute(String commandName, VehicleStorageManager<Vehicle> storage, UserStorageManager userStorageManager,
                                                           String argument, Vehicle el, String userName, Deque history, UserPermission permission) {
        Command instance = new UnknownCommand(storage, argument, el, commandName, userName);
        if(this.commandMap.containsKey(commandName)) {
            try {
                Class<? extends  Command> command = this.commandMap.get(commandName);
                if(AuthorizationCommand.class.isAssignableFrom(command)){

                    instance = (Command) command.getConstructors()[0].newInstance(userStorageManager, userName, argument, permission);
                }
                else if(command.equals(History.class)){
                    instance = (Command) command.getConstructors()[0].newInstance(storage, argument, el, userName, history);
                }
                else if(command.equals(Help.class)){
                    instance = (Command) command.getConstructors()[0].newInstance(storage, argument, el, userName, commandMap);
                }
                else{
                    instance = (Command) command.getConstructors()[0].newInstance(storage, argument, el, userName);
                }
            } catch (Exception e){
                logger.error("Ошибка при создании экземпляра команды", e);
            }

        }
        return instance;

    }
}
