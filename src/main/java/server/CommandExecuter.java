package server;

import dataexchange.Request;
import dataexchange.RequestWithPermission;
import dataexchange.Response;
import lombok.Setter;
import lombok.SneakyThrows;
import objectspace.Vehicle;
import org.slf4j.LoggerFactory;
import server.database.DatabaseStorageManager;
import server.database.UserStorageManager;
import server.database.UserStorageManagerImpl;
import server.database.VehicleStorageManager;
import server.filework.*;
import server.utilities.OutStreamInfoSender;
import server.utilities.Pair;
import сommands.Command;
import сommands.ExecuteScript;
import сommands.UnknownCommand;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.LinkedList;
/**
 * Класс - исполнитель комманд
 * @author Piromant
 */
public class CommandExecuter {

    private static CommandExecuter commandExecuter;
    /** Коллекция объектов типа Vehicle
     * @see Vehicle
     */
    private VehicleStorageManager<Vehicle> storage;
    private UserStorageManager userStorageManager;
    /** Список команд, элементы которого пары вида (Название команды, Объект класса команды)
     * @see Pair
     */
    private static final ch.qos.logback.classic.Logger logger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(CommandExecuter.class);
    private LinkedList<Pair<String, Command>> history;
    private Invoker invoker;
    /** @see FileReader */

    @Setter
    private String userName;

    private LinkedList<String> executedRecursionScript = new LinkedList<>();
    /**
     * Статический метод, предоставляющий доступ к экземпляру класса исполнителя комманд
     */
    @SneakyThrows
    @Deprecated
    public static CommandExecuter getAccess() {
        if(commandExecuter == null)
            commandExecuter = new CommandExecuter();
        return commandExecuter;
    }

    /** Конструктор класса, задающий все параметры и загрудающий коллекцию из файла
     * @see OutStreamInfoSender
     * @see FileInputStreamReader

     */
    @SneakyThrows
    public CommandExecuter() {
        this.invoker = Invoker.getAccess();
        this.history = new LinkedList<>();

        try {
            Config config = Config.getConfig();
            this.storage = DatabaseStorageManager.getAccess(config.getVehiclesDatabasePath(), config.getVehiclesDatabaseUser(),
                                                      config.getVehiclesDatabasePassword(), config.getVehiclesDatabaseName());
            this.userStorageManager = new UserStorageManagerImpl(config.getUserDatabasePath(), config.getUserDatabaseUser(),
                                                                 config.getUserDatabasePassword(), config.getUserDatabaseName());


        } catch (SQLException e) {
            logger.error("Ошибка при работе с базой данных", e);
            throw e;
        }



    }


    public Response executeCommand(RequestWithPermission permitRequest) {

        Request request = permitRequest.request;
        String command_name = request.command_name;
        String argument = request.argument;
        Vehicle v = request.element;
        Response response;

        Command commandToExecute = this.invoker.getCommandToExecute(command_name, this.storage, this.userStorageManager, argument, v,
                                   this.userName, this.history, permitRequest.permission);
        if(!request.sentFromClient) {
            if (commandToExecute instanceof ExecuteScript) {
                if (executedRecursionScript.contains(argument)) {
                    response = new Response("Рекурсия в скрипте! Инструкция пропущена. Скрипт продолжается...");
                    return response;
                }
                this.executedRecursionScript.add(argument);
            }
        } else{
            this.executedRecursionScript.clear();
        }
        response = commandToExecute.execute();
        if(!(commandToExecute instanceof UnknownCommand))
            this.writeCommandToHistory(new Pair<>(command_name, commandToExecute));

        return response;
    }

    /**
     * Метод добавляет команду в историю и если история содержит более 7 элементов удаляет первый.
     * @param command Команда в виде пары (Имя Команды, Объект Команды)
     */
    private void writeCommandToHistory(Pair<String, Command> command){
        if(this.history.size() == 7)
            this.history.removeFirst();
        this.history.add(command);
    }


}
