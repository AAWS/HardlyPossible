package hardlypossible;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.event.MouseEvent;

/**
 *
 * @author Jordan
 */
interface myClickable {

    public void onClick(MouseEvent e);

    public void onPress(MouseEvent e);

    public void onRelease(MouseEvent e);
}
