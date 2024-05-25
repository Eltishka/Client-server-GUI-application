package gui.graphic;

import server.utilities.VehicleOwnerPair;

import java.awt.*;

public class VisualBoat extends VisualVehicle{
    public VisualBoat(Component parent, int width, int height, VehicleOwnerPair element, Color color) {
        super(parent, width, height, element, color);
    }

    @Override
    public void draw(Graphics g, int xStart, int yStart) {
        timer.start();
        Graphics2D g2d = (Graphics2D) g;
        int x = this.x + xStart;
        int y = -this.y + yStart - height;

        // Центрирование и поворот координатной системы
        g2d.translate(x + width / 2, y + height / 2);
        g2d.rotate(angle);
        g2d.translate(-(x + width / 2), -(y + height / 2));

        // Рисование лодки
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + width / 5, y, 3 * width / 5, height / 2);
        g2d.setColor(new Color(214, 10, 124));
        g2d.fillRect(x + width / 5 * 2, y + height / 2, width / 5, height / 4);
        int[] xPoints = {
                x, x + width / 5, x + width / 5 * 2, x + width / 5 * 3, x + width / 5 * 4, x + width,
                x + width / 5 * 4, x + width / 5 * 3, x + width / 5 * 2, x + width / 5
        };
        int[] yPoints = {
                y + 3 * height / 4, y + 3 * height / 4, y + 3 * height / 4, y + 3 * height / 4, y + 3 * height / 4, y + 3 * height / 4,
                y + height, y + height, y + height, y + height
        };
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);

        // Возвращение координатной системы в исходное положение
        g2d.translate(x + width / 2, y + height / 2);
        g2d.rotate(-angle);
        g2d.translate(-(x + width / 2), -(y + height / 2));
    }
}
