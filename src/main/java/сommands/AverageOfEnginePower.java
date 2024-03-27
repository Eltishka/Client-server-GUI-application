package сommands;

import objectspace.Vehicle;
import dataexchange.Response;
import server.database.Storage;

/**

 * Реализация команды average_of_engine_power
 * @author Piromant
 */
public class AverageOfEnginePower extends Command{

    public <T extends Vehicle> AverageOfEnginePower(Storage<T> storage, String argument, T el) {
        super(storage, argument, el);
    }

    /**
     * Метод, выводящий среднее значение силы двигателя по всем элементам коллекции
     */
    @Override
    public Response execute() {
        double res = ((Storage<? super Vehicle>)(this.storage)).stream().mapToDouble(Vehicle::getEnginePower).sum();
        if(res > 0)
             res /= this.storage.size();
        return new Response(res);
    }

    @Override
    public String getHelp() {
        return "Выводит среднее значение поля enginePower для всех элементов коллекции";
    }
}
