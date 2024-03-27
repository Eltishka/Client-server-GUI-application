package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;
import server.filework.FileSaver;
import server.filework.XMLSaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Реализация команды save
 * @author Piromant
 */
public class Save extends Command{
    /**
     * @see FileSaver
     */
    private final FileSaver fileSaver;
    /**
     * Имя файла, в который будет сохранена коллекция
     */
    //private final String fileName;


    public <T extends Vehicle> Save(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
        this.argument = System.getenv("SAVEFILE");
        this.fileSaver = new XMLSaver();
    }

    /**
     * Метод, сохраняющий коллекцию в файл и выводящий что нет, если сохранение не удается
     */
    @Override
    public Response execute() {
        List<String> response = new LinkedList<>();
        try {
            this.fileSaver.save(this.argument, this.storage);
            response.add("Файл сохранен");
        } catch (FileNotFoundException e) {
            File file = new File(this.argument);
            if(!file.exists())
                response.add("Файл не найден");
            else if(!file.canWrite())
                response.add("Не хватает прав для записи файла");
        } catch (NullPointerException e){
            response.add("Не удалось получить инофрмацию об имени файл, возможно переменная окружения SAVEFILE не опрделена");
        }
        catch (Exception e) {
            response.add("Непредвиденная ошибка");
            response.add(e.getMessage());
        }
        return new Response(response.toArray());

    }

    @Override
    public String getHelp() {
        return "Сохраняет коллекцию в файл, имя которого передается путем чтения значения переменной окружения SAVEFILE, оттуда же загружается коллекция при старте программы";
    }
}
