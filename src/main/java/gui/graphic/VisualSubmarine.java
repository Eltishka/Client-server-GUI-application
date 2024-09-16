package gui.graphic;

import server.utilities.VehicleOwnerPair;

import java.awt.*;

public class VisualSubmarine extends VisualVehicle{
    public VisualSubmarine(Component parent, int width, int height, VehicleOwnerPair element, Color color) {
        super(parent, width, height, element, color);
    }

    @Override
    public void draw(Graphics g, int xStart, int yStart) {
        timer.start();
        Graphics2D g2d = (Graphics2D) g;
        int x = this.x + xStart;
        int y = -this.y + yStart;


        g2d.translate(x + width / 2, y - height / 2);
        g2d.rotate(-angle);
        g2d.translate(-(x + width / 2), -(y - height / 2));


        g.setColor(Color.WHITE);
        double xStep = (double) width / 9;
        int yStep = height / 3;
        int[] xPoints = new int[20];
        for (int i = 0; i <= 9; i++) {
            xPoints[i] = x + (int) (i * xStep);
            xPoints[19 - i] = x + (int) (i * xStep);
        }
        int[] yPoints = {
                y, y, y + yStep, y, y, y, y, y, y, y + yStep,
                y + 2 * yStep, y + height, y + height, y + height, y + height, y + height, y + height, y + 2 * yStep, y + height, y + height
        };
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + (int) (7 * xStep), (int) (y + height / 3), height / 3, height / 3);


        g2d.translate(x + width / 2, y - height / 2);
        g2d.rotate(angle);
        g2d.translate(-(x + width / 2), -(y - height / 2));
    }
}
