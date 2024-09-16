package gui.graphic;

import server.utilities.VehicleOwnerPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualDrone extends VisualVehicle {


    public VisualDrone(Component parent, int width, int height, VehicleOwnerPair element, Color color) {
        super(parent, width, height, element, color);

    }

    @Override
    public void draw(Graphics g, int xStart, int yStart) {
        timer.start();
        Graphics2D g2d = (Graphics2D) g;
        int x = this.x + xStart;
        int y = -this.y + yStart;


        g2d.translate(x + width / 2, y - height / 2);
        g2d.rotate(angle);
        g2d.translate(-(x + width / 2), -(y - height / 2));

        g2d.setStroke(new BasicStroke(4.0f));


        g2d.setColor(color);
        g2d.drawOval(x, y - height, width, height);


        g2d.setColor(color);
        g2d.drawLine(x, y - height / 2, x + width, y - height/2);
        g2d.drawLine(x + width/2, y - height, x + width/2 , y);

        g2d.translate(x + width / 2, y - height / 2);
        g2d.rotate(-angle);
        g2d.translate(-(x + width / 2), -(y - height / 2));
    }
}
