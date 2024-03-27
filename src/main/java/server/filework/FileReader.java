package server.filework;

import server.utilities.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 
 * Интерфейс для чтения комманд из файла
 * @author Piromant
 */
public interface FileReader {

    /**
     * Метод для открытия файла
     * @param fileName имя файла для чтения
     * @throws FileNotFoundException
     * @throws NullPointerException в случае, если в качетсве имени файла был передан null
     * @throws SecurityException
     */
    void openFile(String fileName) throws FileNotFoundException, NullPointerException, SecurityException;

    /**
     * Метод, возвращающий список команд, прочитанных из файла
     * @param fileName имя файла для чтения из него команд
     * @return Связный список пар вида (Имя команды; Массив строк для парсинга из них элемента)
     * @throws NullPointerException в случае, если в качетсве имени файла был передан null
     * @throws SecurityException
     * @throws IOException
     */
    LinkedList<Pair<String, ArrayList<String>>> readCommandsFromFile(String fileName) throws NullPointerException, SecurityException, IOException;
    void close() throws IOException;

    /**
     * Метод для чтения всей информации из файла (не в виде команд)
     * @param fileName имя файла для чтения из него команд
     * @return Данные из файла в виде одной строки
     * @throws IOException
     */
    String readWholeFile(String fileName) throws IOException, NullPointerException;
}