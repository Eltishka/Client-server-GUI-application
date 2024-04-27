package сommands;

import dataexchange.RequestWithPermission;
import objectspace.Vehicle;
import objectspace.exceptions.ArgumentVehicleException;
import objectspace.exceptions.VehicleException;
import server.CommandExecuter;
import dataexchange.Request;
import dataexchange.Response;
import server.app.authorization.UserPermission;
import server.database.VehicleStorageManager;
import server.filework.FileInputStreamReader;
import server.filework.FileReader;
import server.utilities.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
/**
 * 
 * Реализация команды execute_script
 * @author Piromant
 */
public class ExecuteScript extends ElementCommand{
    /**
     * @see CommandExecuter
     */
    private CommandExecuter commandExecuter;
    /**
     * @see FileReader
     */
    private FileReader fileReader;


    public <T extends Vehicle> ExecuteScript(VehicleStorageManager storage, String argument, T el, String userName){
        super(storage, argument, el, userName);
        this.commandExecuter = CommandExecuter.getAccess();
        this.fileReader = new FileInputStreamReader();
    }
    /**
     * Метод, считывающий команды с файла и проверяющий их на валидность, затем передающий их в исполнитель команд для исполнения
     */
    @Override
    public Response execute() {

        LinkedList<Pair<String, ArrayList<String>>> commandList = new LinkedList<>();
        try {
            commandList = fileReader.readCommandsFromFile(argument);
        } catch (FileNotFoundException | NullPointerException e) {
            return new Response("Файл не найден");

        } catch (SecurityException e){
            return new Response("Не хватает прав для доступа к файлу");
        } catch (IOException e){
            return new Response(e.getStackTrace());
        }

        ListIterator<Pair<String, ArrayList<String>>> it = commandList.listIterator();
        LinkedList<String> response = new LinkedList<>();
        try {
            while (it.hasNext()) {
                Pair<String, ArrayList<String>> command = it.next();
                Request request = new Request(command.getFirst(), command.getSecond(), userName,false);
                if(command.getFirst().equals("execute_script " + argument))
                    response.add("Скрипт вызывает сам себя. Комманда вызова скрипта в скрипте была пропущена");
                else
                    this.commandExecuter.executeCommand(new RequestWithPermission(request, UserPermission.DefaultUser));
                //this.commandExecuter.executeCommand(command.getFirst(), command.getSecond());
            }
            response.add("Скрипт Выполнен");
        }
        catch (VehicleException e){
            int commandError = it.previousIndex();
            response.add("Скрипт Выполнен до "+ (commandError + 1) + " строки:");
            response.add("Ошибка в команде на " + (commandError + 1) + " строке: " + e.getMessage());
            Throwable cause = e.getCause();
            if(cause instanceof ArgumentVehicleException){
                int errorLine = commandError + ((ArgumentVehicleException) cause).argumentNumber + 1;
                response.add("Строка " + errorLine + ": " + cause.getMessage());
            }
            else if(cause instanceof IllegalArgumentException){
                response.add("Требуется 5 аргументов соответствующих требованиям");
            }
            else{
                response.add("Непредвиденная ошибка");
                response.add(e.getMessage());
            }
        }
        catch (NumberFormatException e){
            int commandError = it.previousIndex();
            response.add("Аргумент команды на " + (commandError + 1) + " строке должен быть целым числом меньшим 2^32");
        }
        return new Response(response.toArray());
    }

    @Override
    public String getHelp() {
        return "Считывает и исполняет скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
