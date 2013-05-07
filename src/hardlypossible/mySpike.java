/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public class mySpike implements myScrollable, myPaintable, myIntersectable {

    private double x, y;
    private int width = 50, height = 50;
    private BufferedImage image;

    public mySpike(double x, double y) {
        this.x = x;
        this.y = y - 50;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.setColor(new Color(255, 255, 255, 240));
        int[] xPoints = new int[]{0, width / 2, width};
        int[] yPoints = new int[]{height, 0, height};

        int[] xPoints2 = new int[]{1, width / 2, width - 1};
        int[] yPoints2 = new int[]{height - 1, 1, height - 1};
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(new Color(0, 0, 0, 250));
        g.fillPolygon(xPoints2, yPoints2, 3);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getXS() {
        return 0;
    }

    @Override
    public double getYS() {
        return 0;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public BufferedImage paint() {
        return image;
    }

    @Override
    public void scroll(double x_amt, double y_amt) {
        x -= x_amt;
        y -= y_amt;
    }
}
