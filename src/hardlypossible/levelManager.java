/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

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

    /*
     * Level one by Malcolm.
     */
    public static void buildLevelOne() {
        build();
        current = 1;
        world.setBackground(3);
        world.setSound(1);
        world.addObjectToWorld(new myActor(world));
        for (int x = 0; x < 100000; x += 50) {
            world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN, true));
        }
        
        for (int x = 0; x < 100000; x += 50) {
            world.addObjectToWorld(new mySurface(x, BOTTOM_SCREEN-300, true));
        }

        world.addObjectToWorld(new mySpike(1175, BOTTOM_SCREEN - 50));
        world.addObjectToWorld(new mySpike(1125, BOTTOM_SCREEN - 50));

        world.addObjectToWorld(new mySpike(2055, BOTTOM_SCREEN - 50));

        world.addObjectToWorld(new myGround(3950, BOTTOM_SCREEN - 50));
        world.addObjectToWorld(new mySpike(4000, BOTTOM_SCREEN - 50));
        world.addObjectToWorld(new mySpike(4050, BOTTOM_SCREEN - 50));
        world.addObjectToWorld(new mySpike(4100, BOTTOM_SCREEN - 50));
        world.addObjectToWorld(new myGround(4150, BOTTOM_SCREEN - 50));
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
            world.built = true;
        }
    }
}
