import client.Client;
import client.Terminal;
import gui.ClientConnectionGUI;
import gui.GUIController;
import gui.actions.*;

import javax.swing.*;
import java.net.ConnectException;

public class ClientStarter {
    public static void main(String[] args) throws ClassNotFoundException, ConnectException {


        Client client = new Client("localhost",  2027);
        client.start();
        GUIController.createNewGuiController(client, "Vehicle App");
        GUIController.registerCommand("add", new AddAction(new ClientConnectionGUI(client)));
        GUIController.registerCommand("info", new InfoAction(new ClientConnectionGUI(client)));
        GUIController.registerCommand("history", new HistoryAction(new ClientConnectionGUI(client)));
        GUIController.registerCommand("average_of_engine_power", new AverageOfEnginePowerAction(new ClientConnectionGUI(client)));
        GUIController.registerCommand("add_if_max", new AddIfMaxAction(new ClientConnectionGUI(client)));
        GUIController.registerCommand("add_if_min", new AddIfMinAction(new ClientConnectionGUI(client)));
        GUIController.registerCommand("clear", new ClearAction(new ClientConnectionGUI(client)));

        GUIController.getAccess().start(client, "Vehicle App");


    }
}
