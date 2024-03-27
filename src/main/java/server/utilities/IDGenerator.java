package server.utilities;

import objectspace.Vehicle;
import server.database.Storage;

import java.util.Random;

/**
 * @author Piromant
 * Генератор id для того, чтобы при создании элементов id был уникальным
 */
public class IDGenerator {
    private static Random randomGenerator = new Random();
    private static Storage storage = new Storage();

    public static Integer generateID(){
        Integer id;
        do {
            id = randomGenerator.nextInt(Integer.MAX_VALUE);
        } while(storage.contains(new Vehicle(id)));
        return id;
    }

    public static void setStorage(Storage storage){
        IDGenerator.storage = storage;
    }
}
