package gui.Panels;

import gui.ClientConnectionGUI;
import gui.GUIController;
import objectspace.Vehicle;
import server.utilities.VehicleOwnerPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class ContentPanel extends JPanel implements ActionListener {
    private JPanel tablePanel;
    private JPanel objectMapPanel;
    private final String userName;
    private final ClientConnectionGUI client;
    private final HashMap<String, Color> usersColors = new HashMap<>();
    private CardLayout cardLayout;
    private static boolean mapOpened = false;
    public ContentPanel(String userName, ClientConnectionGUI client){
        this.userName = userName;
        this.client = client;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(GUIController.resourceBundle.getString("table"))){
            cardLayout.show(this, "Table");
            mapOpened = false;
            System.out.println(mapOpened + " table");
        } else {
            cardLayout.show(this, "Map");
            mapOpened = true;
            System.out.println(mapOpened + " Map");
        }
    }

    public void refresh(Object[] storage){
        this.removeAll();

        cardLayout = new CardLayout();
        setLayout(cardLayout);



        LinkedHashSet<VehicleOwnerPair<Vehicle, String>> vehicles = Arrays.stream(storage)
                .map(o -> (VehicleOwnerPair<Vehicle, String>) o).collect(Collectors.toCollection(LinkedHashSet::new));


        refreshColors(vehicles);
        this.objectMapPanel = new MapPanel(vehicles, this.client, usersColors);

        this.tablePanel = new TablePanel(this.client, storage, userName);
        add(tablePanel, "Table");
        add(objectMapPanel, "Map");


        this.revalidate();
        System.out.println(mapOpened);
        if(mapOpened){
            cardLayout.show(this, "Map");
            System.out.println(mapOpened);
        }
    }

    private void refreshColors(Collection<VehicleOwnerPair<Vehicle, String>> storage){
        Random r = new Random();
        for(VehicleOwnerPair<Vehicle, String> el: storage){
            if(usersColors.containsKey(el.getSecond()))
                continue;
            usersColors.put(el.getSecond(), new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

        }
    }
}
