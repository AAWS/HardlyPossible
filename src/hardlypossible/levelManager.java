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
    private static final int BOTTOM_SCREEN = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 250;

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

    /*
     * Level one by Malcolm.
     */
    public static void buildLevelOne() {
        world.setBackground(1);
        world.setSound(1);


    }

    /*
     * Level two by Paul
     */
    public static void buildLevelTwo() {
        world.setBackground(2);
        world.setSound(2);
    }

    /*
     * Level three by Sabin
     */
    public static void buildLevelThree() {
        world.setBackground(3);
        world.setSound(3);
    }

    /*
     * Level four by Jordan
     */
    public static void buildLevelFour() {
        world.setBackground(4);
        world.setSound(4);
    }
}
