package сommands;

import java.io.Serializable;
import java.util.HashMap;

public class CommandHashMap extends HashMap<String, Class<? extends Command>> implements Serializable {
    public Class<? extends Command> get(String name){
        if(!this.keySet().contains(name))
            return UnknownCommand.class;
        return super.get(name);
    }
}
