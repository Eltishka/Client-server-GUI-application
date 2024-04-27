package сommands;


import lombok.AllArgsConstructor;
import objectspace.Vehicle;
import server.database.VehicleStorageManager;

@AllArgsConstructor
public abstract class ElementCommand implements Command {
    protected VehicleStorageManager storage;
    protected String argument;
    protected Vehicle el;
    protected String userName;
}
