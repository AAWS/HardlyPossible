package hardlypossible;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import image.ResourceTools;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jordan
 */
public class levelManager {

    public static myWorld world;
    private static final int BOTTOM_SCREEN = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9),
            WIDTH = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
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
        add(new myButton(world, 50, 50, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/menu.jpg"),
                Button.Type.TOGGLE));
        world.addObjectToWorld(new myText(world, myText.TextType.ATTEMPTS,
                java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 100, 50));
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
        add(new myButton(world, 150, 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl1.jpg"),
                Button.Type.LEVEL_1));
        add(new myButton(world, 450, 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl2.jpg"),
                Button.Type.LEVEL_2));
        add(new myButton(world, WIDTH - (350 + 300), 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl3.jpg"),
                Button.Type.LEVEL_3));
        add(new myButton(world, WIDTH - 350, 300, (BufferedImage) ResourceTools.loadImageFromResource("resources/images/lvl4.jpg"),
                Button.Type.LEVEL_4));
    }

    /*
     * Level one by Malcolm.
     */
    public static void buildLevelOne() {
        build();
        current = 1;
        world.setBackground(1);
        world.setSound(1);
        world.addObjectToWorld(new myActor(world));
        buildGround();

        add(new mySpike(600, BOTTOM_SCREEN));
        add(new mySpike(650, BOTTOM_SCREEN));
        add(new mySpike(700, BOTTOM_SCREEN));

        add(new mySpike(1000, BOTTOM_SCREEN));
        add(new mySpike(1050, BOTTOM_SCREEN));
        add(new mySpike(1100, BOTTOM_SCREEN));

        add(new mySpike(1400, BOTTOM_SCREEN));
        add(new mySpike(1450, BOTTOM_SCREEN));

        add(new mySpike(1650, BOTTOM_SCREEN));

        add(new myGround(1800, BOTTOM_SCREEN));
        add(new mySpike(1850, BOTTOM_SCREEN));
        add(new mySpike(1900, BOTTOM_SCREEN));
        add(new mySpike(1950, BOTTOM_SCREEN));
        add(new myGround(2000, BOTTOM_SCREEN));

        add(new mySpike(2450, BOTTOM_SCREEN));
        add(new mySpike(2500, BOTTOM_SCREEN));
        add(new mySpike(2550, BOTTOM_SCREEN));
        add(new myGround(2600, BOTTOM_SCREEN));
        add(new mySpike(2650, BOTTOM_SCREEN));
        add(new mySpike(2700, BOTTOM_SCREEN));
        add(new mySpike(2750, BOTTOM_SCREEN));
        add(new myGround(2800, BOTTOM_SCREEN));
        add(new myGround(2850, BOTTOM_SCREEN));
        add(new mySpike(2900, BOTTOM_SCREEN));
        add(new mySpike(2950, BOTTOM_SCREEN));
        add(new mySpike(3000, BOTTOM_SCREEN));
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

        world.addObjectToWorld(new myActor(world));
        for (int x = 0; x < 10000; x += 50) {
            world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN, true));
        }

        world.addObjectToWorld(new myGround(1000, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(1200, BOTTOM_SCREEN - 70));
        world.addObjectToWorld(new myGround(1400, BOTTOM_SCREEN - 120));
        world.addObjectToWorld(new myGround(1600, BOTTOM_SCREEN - 190));
        world.addObjectToWorld(new myGround(1800, BOTTOM_SCREEN - 120));
        world.addObjectToWorld(new myGround(2000, BOTTOM_SCREEN - 50));
        world.addObjectToWorld(new myGround(2200, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(2600, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(3000, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(3050, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(3100, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(3400, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySurface(3450, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3500, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3550, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3600, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new myGround(3650, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(4000, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(4200, BOTTOM_SCREEN - 70));
        world.addObjectToWorld(new myGround(4400, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4450, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4500, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4550, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4600, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4650, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4700, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4750, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4800, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4850, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4900, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(4950, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(5000, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(5050, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new myGround(5250, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5300, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5350, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5400, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5450, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5500, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5550, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5600, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5650, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5700, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5750, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(5800, BOTTOM_SCREEN - 250));
        world.addObjectToWorld(new myGround(6100, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(6100, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(6300, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(6600, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(6850, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7100, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7300, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7350, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7600, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7900, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7950, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(8000, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(8350, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(8600, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(8650, BOTTOM_SCREEN));
        buildStairs(9000, BOTTOM_SCREEN, 8);
        world.addObjectToWorld(new mySpike(11500, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(11550, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(12000, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(12050, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(12100, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(12150, BOTTOM_SCREEN));
        buildSpikestrip(12500, BOTTOM_SCREEN, 3);
        buildSpikestrip(12800, BOTTOM_SCREEN, 2);
        world.addObjectToWorld(new mySpike(13000, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(13400, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(13650, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(13900, BOTTOM_SCREEN));
        buildStairs(14100, BOTTOM_SCREEN, 6);
    }

    /*
     * Level three by Sabin
     */
    public static void buildLevelThree() {
        build();
        current = 3;
        world.setBackground(4);
        world.setSound(3);
        world.addObjectToWorld(new myActor(world));
        for (int x = 0; x < 10000; x += 50) {
            world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN, true));
        }
        world.addObjectToWorld(new mySpike(950, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(1000, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(1200, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(1600, BOTTOM_SCREEN));

        world.addObjectToWorld(new mySurface(1650, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(1700, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(1750, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(1800, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(1850, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(1900, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(1950, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2000, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2050, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2100, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2150, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2200, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2250, BOTTOM_SCREEN, false));

        world.addObjectToWorld(new myGround(1800, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(1850, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(1900, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(1950, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(2000, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(2050, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(2100, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(2150, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(2200, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(2250, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new mySpike(2100, BOTTOM_SCREEN - 150));
        world.addObjectToWorld(new mySpike(2400, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(2450, BOTTOM_SCREEN));

        world.addObjectToWorld(new myGround(2800, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(2850, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(2900, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(2950, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3000, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3050, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3100, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3150, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3200, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3250, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3300, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3350, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3400, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3450, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(3500, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(2800, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(2850, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(2900, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(2950, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3000, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3050, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3100, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3150, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3200, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3250, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(3300, BOTTOM_SCREEN + 50 - 200));

        world.addObjectToWorld(new mySurface(2800, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2850, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2900, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(2950, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3000, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3050, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3100, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3150, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3200, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3250, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3300, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3350, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3400, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3450, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3500, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3550, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3600, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3650, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3700, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3750, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3800, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3850, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3900, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(3950, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4000, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4050, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4100, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4150, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4200, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4250, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4300, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4350, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4400, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4450, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4500, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4550, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4600, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4650, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4700, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4750, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4800, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4850, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4900, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(4950, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(5000, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(5050, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(5100, BOTTOM_SCREEN, false));

        world.addObjectToWorld(new myGround(3715, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(3930, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(4145, BOTTOM_SCREEN + 50 - 250));
        world.addObjectToWorld(new myGround(4360, BOTTOM_SCREEN + 50 - 300));
        world.addObjectToWorld(new myGround(4575, BOTTOM_SCREEN + 50 - 350));
        world.addObjectToWorld(new myGround(4790, BOTTOM_SCREEN + 50 - 400));
        world.addObjectToWorld(new myGround(5005, BOTTOM_SCREEN + 50 - 450));
        world.addObjectToWorld(new mySpike(5450, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(5500, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(5550, BOTTOM_SCREEN));

        world.addObjectToWorld(new mySpike(5700, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(5750, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(5800, BOTTOM_SCREEN));
        world.addObjectToWorld(new myGround(6100, BOTTOM_SCREEN + 50));
        world.addObjectToWorld(new myGround(6100, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new myGround(6100, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(6150, BOTTOM_SCREEN + 50));
        world.addObjectToWorld(new myGround(6150, BOTTOM_SCREEN + 50 - 100));
        world.addObjectToWorld(new mySurface(6200, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6250, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6300, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6350, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6400, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6450, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6500, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6550, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6600, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6650, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6700, BOTTOM_SCREEN, false));
        world.addObjectToWorld(new mySurface(6750, BOTTOM_SCREEN, false));

        world.addObjectToWorld(new myGround(6365, BOTTOM_SCREEN + 50 - 150));
        world.addObjectToWorld(new myGround(6580, BOTTOM_SCREEN + 50 - 200));
        world.addObjectToWorld(new myGround(6720, BOTTOM_SCREEN + 50 - 175));
        world.addObjectToWorld(new myGround(6900, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(6950, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7000, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7050, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7100, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7150, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7200, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7250, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7300, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new myGround(7350, BOTTOM_SCREEN + 50 - 225));
        world.addObjectToWorld(new mySpike(7135, BOTTOM_SCREEN - 275));
        world.addObjectToWorld(new mySpike(7185, BOTTOM_SCREEN - 275));
        world.addObjectToWorld(new mySpike(7235, BOTTOM_SCREEN - 275));
        world.addObjectToWorld(new mySpike(7285, BOTTOM_SCREEN - 275));
        world.addObjectToWorld(new mySpike(7235, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7285, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7185, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7450, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7700, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(7950, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(8200, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(8250, BOTTOM_SCREEN));
        world.addObjectToWorld(new mySpike(8300, BOTTOM_SCREEN));

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
