package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.VehicleStorageManager;
import server.utilities.Pair;

import java.util.Collection;

/**

 * Реализация команды average_of_engine_power
 * @author Piromant
 */
public class AverageOfEnginePower extends ElementCommand{

    public <T extends Vehicle> AverageOfEnginePower(VehicleStorageManager<T> storage, String argument, T el, String userName) {
        super(storage, argument, el, userName);
    }

    /**
     * Метод, выводящий среднее значение силы двигателя по всем элементам коллекции
     */
    @Override
    public Response execute() {
        double res = (((Collection<? extends Pair<Vehicle, String>>)(this.storage.getCollection())).stream()
                .map(Pair::getFirst)).map(Vehicle::new).mapToDouble(Vehicle::getEnginePower).sum();
        if(res > 0)
             res /= this.storage.size();
        return new Response(res);
    }

    @Override
    public String getHelp() {
        return "Выводит среднее значение поля enginePower для всех элементов коллекции";
    }
}
