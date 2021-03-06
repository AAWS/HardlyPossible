package hardlypossible;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public class mySurface implements myScrollable, myPaintable, myIntersectable, myActable {

    private double x, y;
    private int width = 50, height = 4;
    private final int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    private int alpha = 240;
    private BufferedImage image;
    public boolean safe;

    public mySurface(double x, double y, boolean safe) {
        if (!levelManager.world.inLevelView) {
            x -= width;
            y -= height;
        }
        this.x = x;
        this.y = y;
        this.safe = safe;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        if (safe) {
            g.setColor(new Color(255, 255, 255, alpha));
        } else {
            g.setColor(new Color(0, 0, 0, alpha));
        }
        g.fillRect(0, 0, width, height);
    }

    @Override
    public void addedToWorld(myWorld world) {
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
        if(x <= -100) {
        	myWorld.getInstance().removeObjectFromWorld(this);
        }
        y -= y_amt;
    }

    @Override
    public void act() {
        if (x > screenWidth && alpha != 0) {
            alpha = 0;
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            if (safe) {
                g.setColor(new Color(255, 255, 255, alpha));
            } else {
                g.setColor(new Color(0, 0, 0, alpha));
            }
            g.fillRect(0, 0, width, height);
        } else if (x < screenWidth && alpha < 240 && x > width * 2) {
            alpha += 5;
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            if (safe) {
                g.setColor(new Color(255, 255, 255, alpha));
            } else {
                g.setColor(new Color(0, 0, 0, alpha));
            }
            g.fillRect(0, 0, width, height);
        } else if (x <= width && alpha - 4 >= 0) {
            alpha -= 5;
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            if (safe) {
                g.setColor(new Color(255, 255, 255, alpha));
            } else {
                g.setColor(new Color(0, 0, 0, alpha));
            }
            g.fillRect(0, 0, width, height);
        }
    }
}
