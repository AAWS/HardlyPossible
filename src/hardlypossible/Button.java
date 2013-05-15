/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public abstract class Button implements myClickable, myPaintable, MouseListener {

    public int x, y;
    public myWorld world;
    public BufferedImage image;
    public Button.Type type;

    public Button(myWorld world, int x, int y, BufferedImage image, Button.Type type) {
        world.addMouseListener(this);
        this.x = x;
        this.y = y;
        this.world = world;
        this.image = image;
        this.type = type;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= getX() && e.getX() <= getX() + image.getWidth() && e.getY() >= getY() && e.getY() <= getY() + image.getHeight()) {
            onPress(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getX() >= getX() && e.getX() <= getX() + image.getWidth() && e.getY() >= getY() && e.getY() <= getY() + image.getHeight()) {
            onRelease(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() >= getX() && e.getX() <= getX() + image.getWidth() && e.getY() >= getY() && e.getY() <= getY() + image.getHeight()) {
            onClick(e);
        }
    }

    public enum Type {

        TOGGLE, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, TO_MENU;
    }
}
