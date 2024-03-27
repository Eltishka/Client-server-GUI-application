package server;

import dataexchange.Request;
import dataexchange.Response;
import objectspace.Vehicle;
import server.database.Storage;
import server.filework.*;
import server.utilities.InfoSender;
import server.utilities.OutStreamInfoSender;
import server.utilities.Pair;
import сommands.Command;
import сommands.ExecuteScript;
import сommands.Save;
import сommands.UnknownCommand;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
/**
 * Класс - исполнитель комманд
 * @author Piromant
 */
public class CommandExecuter {

    private static CommandExecuter server;
    /** Коллекция объектов типа Vehicle
     * @see Vehicle
     */
    private Storage<Vehicle> storage;

    /** Список команд, элементы которого пары вида (Название команды, Объект класса команды)
     * @see Pair
     */
    private LinkedList<Pair<String, Command>> history;
    private Invoker invoker;
    /** @see FileReader */
    private FileReader fileReader;
    /** @see InfoSender */
    private InfoSender infoSender;
    /** @see FileSaver */
    private FileSaver fileSaver;

    private LinkedList<String> executedRecursionScript = new LinkedList<>();
    /**
     * Статический метод, предоставляющий доступ к экземпляру класса исполнителя комманд
     */
    public static CommandExecuter getAccess(){
        if(server == null)
            server = new CommandExecuter();
        return server;
    }

    /** Конструктор класса, задающий все параметры и загрудающий коллекцию из файла
     * @see OutStreamInfoSender
     * @see FileInputStreamReader
     * @see XMLSaver
     * @see XMLLoader
     */
    private CommandExecuter()  {
        this.invoker = Invoker.getAccess();
        this.history = new LinkedList<>();
        this.infoSender = new OutStreamInfoSender();
        this.fileReader = new FileInputStreamReader();
        this.fileSaver = new XMLSaver();
        try {
            this.storage = (new XMLLoader(fileReader, System.getenv("SAVEFILE"))).loadStorage();
        } catch (IOException | ParseException e){
            File file = new File(System.getenv("SAVEFILE"));
            if(!file.exists())
                this.infoSender.sendLine("Невозможно загрузить коллекцию из файла. Файл не существует");
            else if(!file.canRead())
                this.infoSender.sendLine("Невозможно загрузить коллекцию из файла. Не хватает прав на чтение файла");
            else
                this.infoSender.sendLine("Невозможно загрузить коллекцию из файла. Ошибка в xml-тэгах");
            this.storage = new Storage<>();
        } catch(NullPointerException e){
            this.infoSender.sendLine("Невозможно загрузить коллекцию из файла. Переменная SAVEFILE не определена");
        } catch (Exception e){
            this.infoSender.sendLine("Невозможно заргузить коллекцию. Ошибка в файле");
            this.storage = new Storage<>();
        }


    }

    /**
     * Метод, который выбирает команду по ее названию, исполняет ее и записывает в историю команд.
     * Также здесь происходит парсинг объекта типа Vehicle из строк
     * @param request запрос
     */
    public Response executeCommand(Request request/*String command, ArrayList<String> element*/) {

        String command_name = request.command_name;
        String argument = request.argument;
        Vehicle v = request.element;
        Response response = new Response();

        Command commandToExecute = this.invoker.getCommandToExecute(command_name, this.storage, argument, v, this.history);
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

    public void externalSave(){
        (new Save(this.storage, "", null)).execute();
    }

}
