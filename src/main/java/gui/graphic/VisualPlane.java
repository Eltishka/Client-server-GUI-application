package gui.graphic;

import server.utilities.VehicleOwnerPair;

import java.awt.*;

public class VisualPlane extends VisualVehicle{
    public VisualPlane(Component parent, int width, int height, VehicleOwnerPair element, Color color) {
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

        int xStep = width / 16;
        int yStep = height / 14;
        int[] xPoints = {
                x, x + 2 * xStep, x + 6 * xStep,
                x + 6 * xStep, x + 7 * xStep, x + xStep * 9, x + xStep * 10,
                x + xStep * 10, x + xStep * 14, x + width, /**/
                x + xStep * 14, x + xStep * 12, x + xStep * 10, x + xStep * 10,
                x + xStep * 9, x + xStep * 11, x + xStep * 12, x + xStep * 9,
                x + xStep * 8, x + xStep * 7, x + xStep * 4, x + xStep * 5, x + xStep * 7,
                x + xStep * 6, x + xStep * 6, x + xStep * 4, x + xStep * 2
        };
        int[] yPoints = {
                y - yStep * 7, y - yStep * 5, y - yStep * 3, y - yStep * 2,
                y, y, y - yStep * 2, y - yStep * 3, y - yStep * 5, y - yStep * 7,
                y - yStep * 7, y - yStep * 6, y - yStep * 6, y - yStep * 10,
                y - yStep * 12, y - yStep * 13, y - height, y - height,
                y - yStep * 13, y - height, y - height, y - yStep * 13, y - yStep * 12,
                y - yStep * 10, y - yStep * 6, y - yStep * 6, y - yStep * 7
        };
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);

        g2d.translate(x + width / 2, y - height / 2);
        g2d.rotate(-angle);
        g2d.translate(-(x + width / 2), -(y - height / 2));
    }

}
