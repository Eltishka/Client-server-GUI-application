package dataexchange;

import objectspace.Vehicle;

import java.io.Serial;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Реквест хранит четыре поля: имя команды, ее аргумент, элемент и отправителя
 */
public class Request implements Serializable {
    public final String command_name;
    public final String argument;
    public final Vehicle element;
    public final String userName;
    public final boolean sentFromClient;

    @Serial
    public final static long serialVersionUID = 2311231232134421989L;

    public Request(String command, ArrayList<String> element, String userName, boolean sentFromClient) {
        String[] commandParts = command.split(" ");
        this.command_name = commandParts[0];

        Vehicle v = null;
        if(element.size() > 0)
            v = Vehicle.parseVehicle(element.toArray(new String[5]));

        this.element = v;

        if(commandParts.length > 1)
            this.argument = commandParts[1];
        else
            this.argument = "";
        this.sentFromClient = sentFromClient;
        this.userName = userName;
    }

    public Request(Request copy){
        this.command_name = copy.command_name;
        this.element = copy.element;
        this.argument = copy.argument;
        this.sentFromClient = copy.sentFromClient;
        this.userName = copy.userName;
    }

}
