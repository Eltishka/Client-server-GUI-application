package gui.graphic;

import dataexchange.Request;
import dataexchange.Response;
import gui.GUIController;
import gui.Panels.VehicleChangePanel;
import gui.Panels.VehicleForm;
import lombok.Getter;
import objectspace.Vehicle;
import server.utilities.VehicleOwnerPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public abstract class VisualVehicle {
    @Getter
    protected int x;
    @Getter
    protected int y;
    protected int width;
    protected int height;
    @Getter
    protected VehicleOwnerPair<Vehicle, String> element;

    protected Color color;

    protected double angle = 0;
    protected Timer timer;

    private Component parent;

    public VisualVehicle(Component parent, int width, int height, VehicleOwnerPair<Vehicle, String> element, Color color){
        this.x = element.getFirst().getCoordinates().getX().intValue();
        this.y = element.getFirst().getCoordinates().getY().intValue();
        this.width = width;
        this.height = height;
        this.element = element;
        this.color = color;
        this.parent = parent;

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angle += Math.PI / 60;  // увеличиваем угол
                if (angle >= 2 * Math.PI) {
                    angle = 0;  // сбрасываем угол, если он превысил полный круг
                    timer.stop();  // останавливаем таймер после первого круга
                }
                parent.repaint();
            }
        });
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
    }



    public abstract void draw(Graphics g, int xStart, int yStart);
}
