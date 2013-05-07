/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import java.awt.Point;

/**
 *
 * @author Jordan
 */
public class levelManager {

    private static myWorld world;
    private static final int BOTTOM_SCREEN = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9);
    private static int current = 1;

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

    public static void build() {
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
    /*
     * Level one by Malcolm.
     */

    public static void buildLevelOne() {
        build();
        current = 1;
        world.setBackground(4);
        world.setSound(4);
        world.addObjectToWorld(new myActor(world, "NPCGOD"));
        for (int x = 50; x < 57500; x += 50) {
            world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN, true));
        }

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
    }

    private static Point buildStairs(int start_x, int start_y, int size) {
        int lastx = start_x, lasty = start_y;
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
        return new Point(lastx, lasty);
    }

    private static Point buildPlatform(int start_x, int start_y, int length) {
        int original = start_x;
        while (start_x < original + length * 50) {
            add(new myGround(start_x, start_y));
            start_x += 50;
        }
        return new Point(start_x, start_y);
    }

    private static Point buildSpikestrip(int start_x, int start_y, int length) {
        int original = start_x;
        while (start_x < original + length * 50) {
            add(new mySpike(start_x, start_y));
            start_x += 50;
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
        world.setBackground(4);
        world.setSound(4);
    }

    private static class buildThread extends Thread {

        private int level;

        public buildThread(int level) {
            this.level = level;
        }

        @Override
        public void run() {
            switch (level) {
                case 1:
                    buildLevelOne();
                    break;
                case 2:
                    buildLevelTwo();
                    break;
                case 3:
                    buildLevelThree();
                    break;
                case 4:
                    buildLevelFour();
                    break;
            }
            for (int delay = 0; delay < 100000; delay++) {
            }
            world.built = true;
        }
    }
}
