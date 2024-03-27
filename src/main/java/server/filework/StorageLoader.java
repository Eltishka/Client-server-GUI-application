package server.filework;

import server.database.Storage;

import java.io.IOException;
import java.text.ParseException;

/**
 * Интерфейс для загрузки сохраненной коллекции
 * @author Piromant
 */
public interface StorageLoader {
    /**
     * Загружает сохраненную коллекцию
     * @return Загруженная коллекцмя
     * @throws IOException
     * @throws ParseException
     */
    Storage loadStorage() throws IOException, ParseException;
}
