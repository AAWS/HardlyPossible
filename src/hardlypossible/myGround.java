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
public class myGround implements myScrollable, myPaintable, myIntersectable {

    private double x, y;
    private int width = 50, height = 50;
    public boolean fake;
    private BufferedImage image;

    public myGround(double x, double y) {
        this(x, y, false);
    }

    public myGround(double x, double y, boolean trick) {
        if (!levelManager.world.inLevelView) {
            x -= width;
            y -= height;
        }
        this.x = x;
        this.y = y - 50;
        this.fake = trick;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(new Color(255, 255, 255, 240));
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(0, 0, 0, 250));
        g.fillRect(1, 1, width - 2, height - 2);
    }

    @Override
    public double getX() {
        return levelManager.world.inLevelView ? x : x + getWidth() / levelManager.world.levelFactor;
    }

    @Override
    public double getY() {
        return levelManager.world.inLevelView ? y : y + getHeight() / levelManager.world.levelFactor;
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
        return levelManager.world.inLevelView ? width : width / levelManager.world.levelFactor;
    }

    @Override
    public int getHeight() {
        return levelManager.world.inLevelView ? height : height / levelManager.world.levelFactor;
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
