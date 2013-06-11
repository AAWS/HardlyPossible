package hardlypossible;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public interface myPaintable {

    public double getX();

    public double getY();

    public double getXS();

    public double getYS();

    public int getWidth();

    public int getHeight();

    public BufferedImage paint();
}
