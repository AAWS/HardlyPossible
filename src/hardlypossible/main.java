
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import environment.ApplicationStarter;
import environment.Environment;
import image.ResourceTools;
import java.awt.Dimension;
import java.awt.Image;

/**
 *
 * @author Jordan
 */
public class main {

    final static int width = 640, height = 480;

    public static void main(String[] args) {
        start(args);
    }

    private static void start(String[] args) {
        String appName = "The Impossible Game";
        Image background = ResourceTools.loadImageFromResource("resources/images/background.jpg");
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        background = background.getScaledInstance(screenSize.width, screenSize.height, 1);
        Dimension appSize = new Dimension(screenSize.width, screenSize.height);
        Environment environment = new myWorld(background);
        ApplicationStarter.run(args, appName, appSize, environment);
    }
}