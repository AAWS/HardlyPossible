/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import image.ResourceTools;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public class levelManager {

    public static myWorld world;
    private static final int BOTTOM_SCREEN = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9), WIDTH = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static int current = 0;

    /* HOW TO BUILD A LEVEL.
     * 
     * First of all, you need to call world.setBackground(int) to set the background image.
     * Then you need to call world.setSound(int) to set the soundtrack.
     * 
     * To add objects to the world, you must call world.addObjectToWorld(new <what you want to add>);
     * 
     * The constructors are as follows:
     * 
     * To add an actor:
     * new myActor(world)
     * 
     * To add the standard ground object:
     * new mySurface(x, y, <true/false>)
     * 
     * The true or false parameter for mySurface states whether the ground is safe (true) or unsafe (false)
     * 
     * To add the squares that you have to jump on top of:
     * new myGround(x, y)
     * 
     * To add a spike:
     * new mySpike(x, y)
     * 
     * Each block is 50x50 pixels. BOTTOM_SCREEN is the initrial ground level. It should be your y-value until you start staircases.
     * 
     * To test your level, go to initializeEnvironment() in myWorld and change levelManager.build
     */
    public static void initialize(myWorld main) {
        world = main;
    }

    public static void set(int level) {
        current = level;
        buildCurrent();
    }

    private static void build() {
        add(new myButton(world, 50, 50, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/menu.jpg"), Button.Type.TOGGLE));
        world.addObjectToWorld(new myText(world, myText.TextType.ATTEMPTS, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 100, 50));
    }

    public static void reset() {
        world.reset();
    }

    public static void buildCurrent() {
        new buildThread(current).start();
    }

    private static void add(Object o) {
        world.addObjectToWorld(o);
    }

    public static void toggle() {
        world.built = !world.built;
        world.jsound.pauseToggle();
    }

    public static void buildMenu() {
        current = 0;
        world.setBackground(1);
        world.setSound(0);
        buildDisplay();
        add(new myButton(world, 150, 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl1.jpg"), Button.Type.LEVEL_1));
        add(new myButton(world, 450, 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl2.jpg"), Button.Type.LEVEL_2));
        add(new myButton(world, WIDTH - (350 + 300), 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl3.jpg"), Button.Type.LEVEL_3));
        add(new myButton(world, WIDTH - 350, 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl4.jpg"), Button.Type.LEVEL_4));
    }

    /*
     * Level one by Malcolm.
     */
    public static void buildLevelOne() {
        build();
        current = 1;
        world.setBackground(1);
        world.setSound(1);
    }

    private static void buildGround() {
        if (world.inLevelView) {
            for (int x = 50; x < 57500; x += 50) {
                world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN, true));
            }
        } else {
            for (int x = 50 / world.levelFactor; x < 57500 / world.levelFactor; x += 50 / world.levelFactor) {
                world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN, true));
            }
        }
    }

    private static Point buildStairs(int start_x, int start_y, int size) {
        int lastx = start_x, lasty = start_y;
        if (world.inLevelView) {
            for (int a = 0; a < size; a++) {
                add(new myGround(start_x + (250 * a), start_y - (50 * a)));
                add(new mySurface(start_x + (250 * a), BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 * a) + 50, BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 * a) + 100, BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 * a) + 150, BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 * a) + 200, BOTTOM_SCREEN, false));
                lastx = start_x + (250 * a);
                lasty = start_y - (50 * a);
            }
        } else {
            for (int a = 0; a < size; a++) {
                add(new myGround(start_x + (250 / world.levelFactor * a), start_y - (50 / world.levelFactor * a)));
                add(new mySurface(start_x + (250 / world.levelFactor * a), BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 / world.levelFactor * a) + 50, BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 / world.levelFactor * a) + 100, BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 / world.levelFactor * a) + 150, BOTTOM_SCREEN, false));
                add(new mySurface(start_x + (250 / world.levelFactor * a) + 200, BOTTOM_SCREEN, false));
                lastx = start_x + (250 / world.levelFactor * a);
                lasty = start_y - (50 / world.levelFactor * a);
            }
        }
        return new Point(lastx, lasty);
    }

    private static Point buildPlatform(int start_x, int start_y, int length) {
        int original = start_x;
        if (world.inLevelView) {
            while (start_x < original + length * 50) {
                add(new myGround(start_x, start_y));
                start_x += 50;
            }
        } else {
            while (start_x < original + (length * 50 / world.levelFactor)) {
                add(new myGround(start_x, start_y));
                start_x += 50 / world.levelFactor;
            }
        }
        return new Point(start_x, start_y);
    }

    private static Point buildSpikestrip(int start_x, int start_y, int length) {
        int original = start_x;
        if (world.inLevelView) {
            while (start_x < original + length * 50) {
                add(new mySpike(start_x, start_y));
                start_x += 50;
            }
        } else {
            while (start_x < original + (length * 50 / world.levelFactor)) {
                add(new mySpike(start_x, start_y));
                start_x += 50 / world.levelFactor;
            }
        }
        return new Point(start_x, start_y);
    }

    /*
     * Level two by Paul
     */
    public static void buildLevelTwo() {
        build();
        current = 2;
        world.setBackground(2);
        world.setSound(2);
    }

    /*
     * Level three by Sabin
     */
    public static void buildLevelThree() {
        build();
        current = 3;
        world.setBackground(3);
        world.setSound(3);
    }

    /*
     * Level four by Jordan
     */
    public static void buildLevelFour() {
        build();
        current = 4;
        world.setBackground(5);
        world.setSound(4);
        world.addObjectToWorld(new myActor(world));
        buildGround();

        add(new myGround(4100, BOTTOM_SCREEN));
        add(new myGround(4350, BOTTOM_SCREEN - 50));
        add(new myGround(4500, BOTTOM_SCREEN));
        Point p = buildStairs(4750, BOTTOM_SCREEN - 50, 10);
        Point po = buildPlatform(p.x, p.y, 10);
        add(new mySpike(po.x - (50 * 5), p.y - 50));
        Point pop = buildSpikestrip(po.x + (int) (50 * 2.5), BOTTOM_SCREEN, 3);
        Point popo = buildSpikestrip(pop.x + (50 * 3), BOTTOM_SCREEN, 3);
        add(new myGround(popo.x + (50 * 4), BOTTOM_SCREEN));
        Point popop = buildPlatform(popo.x + (50 * 4), BOTTOM_SCREEN - 50, 4);
        buildPlatform(popo.x + (50 * 4), BOTTOM_SCREEN - (50 * 3), 4);
        Point popopo = buildStairs(popop.x, popop.y, 5);
        buildPlatform(popopo.x + 50, popopo.y, 3);
        add(new mySpike(popopo.x + 150, popopo.y - 50));
        add(new mySpike(popopo.x + 350, BOTTOM_SCREEN));
        add(new mySpike(popopo.x + 900, BOTTOM_SCREEN - 50));
        add(new myGround(popopo.x + 900, BOTTOM_SCREEN));
        add(new myGround(popopo.x + 850, BOTTOM_SCREEN));
    }

    /*
     * Display level
     */
    public static void buildDisplay() {
        world.addObjectToWorld(new myActor(world, "INVERTAUTOGOD"));
        buildGround();

        add(new myGround(4100, BOTTOM_SCREEN));
        Point po = buildStairs(4750, BOTTOM_SCREEN - 50, 7);
        Point popop = buildPlatform(po.x + (50 * 4), BOTTOM_SCREEN - 50, 4);
        Point popopo = buildStairs(popop.x, popop.y, 5);
        buildPlatform(popopo.x + 50, popopo.y, 3);
        add(new mySpike(popopo.x + 150, popopo.y - 50));
        add(new mySpike(popopo.x + 350, BOTTOM_SCREEN));
        add(new myGround(popopo.x + 900, BOTTOM_SCREEN));
        add(new myGround(popopo.x + 850, BOTTOM_SCREEN));
    }

    private static class buildThread extends Thread {

        private int level;
        private boolean alerted = false;

        public buildThread(int level) {
            this.level = level;
        }

        @Override
        public void run() {
            if (world.building) {
                return;
            }
            world.built = false;
            world.building = true;
            switch (level) {
                case 0:
                    buildMenu();
                    alerted = true;
                    break;
                case 1:
                    buildLevelOne();
                    alerted = true;
                    break;
                case 2:
                    buildLevelTwo();
                    alerted = true;
                    break;
                case 3:
                    buildLevelThree();
                    alerted = true;
                    break;
                case 4:
                    buildLevelFour();
                    alerted = true;
                    break;
            }
            while (!alerted) {
            }
            alerted = false;
            world.built = true;
            world.building = false;
        }
    }
}
