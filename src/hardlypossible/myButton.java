/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public class myButton extends Button {

    public myButton(myWorld world, int x, int y, BufferedImage img, Button.Type type) {
        super(world, x, y, img, type);
    }

    @Override
    public void onClick(MouseEvent e) {
        switch (type) {
            case LEVEL_1:
                levelManager.current = 1;
                break;
            case LEVEL_2:
                levelManager.current = 2;
                break;
            case LEVEL_3:
                levelManager.current = 3;
                break;
            case LEVEL_4:
                levelManager.current = 4;
                break;
            case TOGGLE:
                levelManager.toggle();
                break;
            case TO_MENU:
                levelManager.current = 0;
                break;
        }
        switch (type) {
            case LEVEL_1:
            case LEVEL_2:
            case LEVEL_3:
            case LEVEL_4:
            case TO_MENU:
                levelManager.reset();
                levelManager.buildCurrent();
                break;
            case TOGGLE:
                break;
        }
    }

    @Override
    public void onPress(MouseEvent e) {
        switch (type) {
            case LEVEL_1:
                break;
            case LEVEL_2:
                break;
            case LEVEL_3:
                break;
            case LEVEL_4:
                break;
            case TOGGLE:
                break;
            case TO_MENU:
                break;
        }
    }

    @Override
    public void onRelease(MouseEvent e) {
        switch (type) {
            case LEVEL_1:
                break;
            case LEVEL_2:
                break;
            case LEVEL_3:
                break;
            case LEVEL_4:
                break;
            case TO_MENU:
                break;
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
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public BufferedImage paint() {
        return image;
    }
}
