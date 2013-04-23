/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public class myText implements myPaintable {

    private TextType tracking;
    private myWorld world;
    private BufferedImage image;
    private String str, slug;
    private double x, y;

    public myText(myWorld world, TextType tracking, double x, double y) {
        this.tracking = tracking;
        this.world = world;
        this.x = x;
        this.y = y;
        if (tracking == TextType.ATTEMPTS) {
            image = new BufferedImage(300, 60, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            g.setFont(new Font("monospaced", Font.BOLD, 30));
            str = "Attempts: ";
            slug = "" + world.attempts;
            g.drawString(str + slug, 0, 20);
        }
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
        return paint().getWidth();
    }

    @Override
    public int getHeight() {
        return paint().getHeight();
    }

    @Override
    public BufferedImage paint() {
        return image;
    }

    public static enum TextType {

        ATTEMPTS
    }
}
