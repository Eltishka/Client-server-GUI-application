package сommands;
import lombok.AllArgsConstructor;
import objectspace.Vehicle;
import dataexchange.Response;
import server.database.UserStorageManager;
import server.database.VehicleStorageManager;
import server.utilities.Pair;

import java.util.Deque;
import java.util.Map;


/**
 * 
 * Абстрактный класс команды, который реализуют все команды
 * @author Piromant
 */
public interface Command {

    Response execute();
    String getHelp();

}
