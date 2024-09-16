package gui.Panels;

import gui.ClientConnectionGUI;
import gui.GUIController;
import gui.actions.UpdateAction;
import gui.graphic.*;
import objectspace.Coordinates;
import objectspace.Vehicle;
import server.utilities.VehicleOwnerPair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import static java.lang.Math.*;

public class MapPanel extends JPanel implements MouseMotionListener {
    private LinkedHashSet<VisualVehicle> objects = new LinkedHashSet<>();
    private int step;
    private int xStart = 0;
    private int yStart = 0;
    private int curMouseX = 0;
    private int curMouseY = 0;
    private int x;
    private int y;
    private HashMap<String, Color> usersColors;
    private JFormattedTextField curX, curY;

    private final ClientConnectionGUI client;

    public MapPanel(LinkedHashSet<VehicleOwnerPair<Vehicle, String>> objects, ClientConnectionGUI client, HashMap<String, Color> usersColors) {
        super();
        this.client = client;
        for(VehicleOwnerPair<Vehicle, String> obj: objects){

            this.objects.add(
                    switch (obj.getFirst().getType()){
                        case DRONE -> new VisualDrone(this,50, 50, obj, usersColors.get(obj.getSecond()));
                        case PLANE -> new VisualPlane(this,50, 45, obj, usersColors.get(obj.getSecond()));
                        case SUBMARINE -> new VisualSubmarine(this,45, 15, obj, usersColors.get(obj.getSecond()));
                        case BOAT -> new VisualBoat(this,50, 40, obj, usersColors.get(obj.getSecond()));
                    });
        }
        Double maxX = objects.stream()
                .map(VehicleOwnerPair::getFirst)
                .map(Vehicle::getCoordinates)
                .map(Coordinates::getX)
                .max(Double::compareTo)
                .get();
        Double maxY = Double.valueOf(objects.stream()
                .map(VehicleOwnerPair::getFirst)
                .map(Vehicle::getCoordinates)
                .map(Coordinates::getY)
                .max(Long::compareTo)
                .get());
        this.step = 10;//Double.min(this.getPreferredSize().height, this.getPreferredSize().width) / Double.max(maxX, maxY);
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                MapPanel.this.mouseDragged(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    MapPanel.this.xStart = e.getX();
                    MapPanel.this.yStart = e.getY();
                }
            }
            @Override
            public void mouseClicked(MouseEvent e){
                VisualVehicle element = MapPanel.this.objects.stream()
                        .filter((el) -> el.contains(e.getX() - x, MapPanel.this.getHeight() - e.getY() + y))
                        .findFirst()
                        .orElse(null);
                if(element != null) {
                    VehicleChangePanel form = new VehicleChangePanel(element.getElement());
                    int result = JOptionPane.showOptionDialog(
                            null,
                            form,
                            GUIController.resourceBundle.getString("update"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            new String[]{GUIController.resourceBundle.getString("update")},
                            GUIController.resourceBundle.getString("update")
                    );
                    if(result == JOptionPane.YES_NO_OPTION){
                        if(form.checkValidity()){
                            ArrayList<String> el = new ArrayList<>();
                            el.add(form.getNameValue());
                            el.add(form.getСoordXValue() + " " + form.getСoordYValue());
                            el.add(form.getEnginePowerValue());
                            el.add(form.getTypeValue());
                            el.add(form.getFuelTypeValue());
                            new UpdateAction(MapPanel.this.client, el, element.getElement().getFirst().getId().toString()).actionPerformed(null);
                        }else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    GUIController.resourceBundle.getString("invalid_data_message"),
                                    GUIController.resourceBundle.getString("error"),
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });

        JMenuBar menu = new JMenuBar();
        this.curX = new JFormattedTextField();
        this.curY = new JFormattedTextField();
        this.curX.setPreferredSize(new Dimension(100, 20));
        this.curY.setPreferredSize(new Dimension(100, 20));
        menu.add(this.curX);
        menu.add(this.curY);
        add(menu);

        this.curX.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    MapPanel.this.x = min(-Integer.parseInt(curX.getText()), 0);
                    repaint();
                }catch (NumberFormatException exception){
                    curX.setText("0");
                }

            }
            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}

        });

        this.curY.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    MapPanel.this.y = max(Integer.parseInt(curY.getText()), 0);
                    repaint();
                }catch (NumberFormatException exception){
                    curY.setText("0");
                }

            }
            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}

        });

    }

    private void drawGrid(Graphics g) {

        g.setColor(Color.LIGHT_GRAY);
        for (int y = 0; y < getHeight() + abs(this.y); y += this.step) {
            g.drawLine(0, y, getWidth() + abs(this.y), y);
        }

        for (int x = 0; x < getWidth() + abs(this.x); x += this.step) {
            g.drawLine(x, 0, x, getHeight() + abs(this.x));
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        for (VisualVehicle object : objects) {
            object.draw(g, x, +getHeight() + y);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.curMouseX = e.getX();
        this.curMouseY = e.getY();
        this.x -= this.xStart - this.curMouseX ;
        this.y -= this.yStart - this.curMouseY;
        this.x = min(this.x, 0);
        this.y = max(this.y, 0);
        this.xStart = e.getX();
        this.yStart = e.getY();
        this.curX.setText(String.valueOf(abs(this.x)));
        this.curY.setText(String.valueOf(abs(this.y)));

        //System.out.println(this.xStart + " " + this.curMouseX);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
