/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import image.ResourceTools;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

/**
 *
 * @author Jordan
 */
public class myActor implements myPaintable, myIntersectable, myActable {

    private myWorld m;
    private double x, y, ys, rotation;
    private int width = 50, height = 50;
    private final double GRAVITY = 0.4, MAX_GRAVITY = 8.9, JUMPSTR = 8.8, ROTATIONSPEED = 0.061, TOP_SCREEN = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3, BOTTOM_SCREEN = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8;
    private BufferedImage image;
    private boolean dead, auto, invinsible;

    public myActor(myWorld m, double x, double y) {
        this.m = m;
        this.x = x - 3;
        if (y == 0.0000001) {
            y = BOTTOM_SCREEN - 50;
            this.y = y;
        }
        /*
         * Cache the image.
         */
        image = (BufferedImage) ResourceTools.loadImageFromResource("resources/images/actor.jpg");
    }

    public myActor(myWorld world) {
        this(world, 100, 0.0000001);
    }

    public myActor(myWorld world, String id) {
        this.m = world;
        this.x = 100;
        this.y = BOTTOM_SCREEN - 50;
        id = id.toUpperCase() + "";
        this.auto = id.contains("NPC");
        this.invinsible = id.contains("GOD");
        /*
         * Cache the image.
         */
        image = (BufferedImage) ResourceTools.loadImageFromResource("resources/images/actor.jpg");
    }

    @Override
    public void act() {
        if (!onGround(0) && onBlock(0) == null) {
            /*
             * Not on ground so add gravity and rotation.
             */
            if (MAX_GRAVITY > 0) {
                if (ys <= MAX_GRAVITY) {
                    ys += GRAVITY;
                }
            } else {
                if (ys > MAX_GRAVITY) {
                    ys += GRAVITY;
                }
            }
            rotation += ROTATIONSPEED;
        } else if (onGround(0) || onBlock(0) != null) {
            /*
             * On ground so zero rotation and vertical velocity.
             */
            rotation = 0;
            ys = 0;
        }
        /*
         * Jump.
         */
        if (m.isKeyDown("space") && (onGround(0) || onBlock(0) != null) && !auto) {
            jump();
        }

        /*
         * LOGIC.
         */
        if (auto && (onGround(0) || onBlock(0) != null) && m.isKeyDown("space")) {
            if ((onGround(129, -5) && !onGround(20, -50)) || hitSpike(20) || (onGround(129, -70) && !onGround(20, -70)) || (onGround(0) && !onGround(50, 50)) || (onBlock(40) != null && !onBlock(40).safe)) {
                jump();
            }
        }

        hitSpike(0);
        if (dead) {
            return;
        }

        /*
         * Move.
         */
        y += ys;
        if (y <= TOP_SCREEN) {
            m.scrollY = -m.SCROLLSPEED / 2;
        } else if (y >= BOTTOM_SCREEN - 100) {
            m.scrollY = m.SCROLLSPEED * 2;
        }
    }

    private void jump() {
        if (MAX_GRAVITY > 0) {
            ys = -JUMPSTR;
        } else {
            ys = JUMPSTR;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Support Methods">
    /**
     * Determine whether or not the actor is on a ground block.
     *
     * @return Whether or not the actor is on a ground block.
     */
    private boolean onGround(int xoff) {
        if (dead) {
            return false;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle actor = new Rectangle();
        if (MAX_GRAVITY > 0) {
            actor.setBounds((int) x + xoff, (int) y + 3, width, height);
        } else {
            actor.setBounds((int) x + xoff, (int) y - 3, width, height);
        }

        for (myIntersectable i : myWorld.inIntersectable) {
            /*
             * Loop through world objects and check for those intersectable.
             */
            if (!i.equals(this) && i instanceof myGround) {
                /*
                 * Cast the intersectable object to paintable and create it's rectangle.
                 */
                myPaintable p = (myPaintable) i;
                Rectangle other = new Rectangle();
                other.setBounds((int) p.getX(), (int) p.getY(), p.getWidth(), p.getHeight());
                if (actor.intersects(other) && !((myGround) i).fake) {
                    if (xoff == 0) {
                        /*
                         * Snap to the ground block and return true.
                         */
                        double othery = other.getY();
                        if (y >= othery || (y <= othery && ys < 0)) {
                            die();
                            return false;
                        }
                        if (MAX_GRAVITY > 0) {
                            y = othery - height;
                        } else {
                            y = othery + height;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determine whether or not the actor is on a ground block.
     *
     * @return Whether or not the actor is on a ground block.
     */
    private mySurface onBlock(int xoff) {
        if (dead) {
            return null;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle actor = new Rectangle();
        if (MAX_GRAVITY > 0) {
            actor.setBounds((int) x + xoff, (int) y + 3, width, height);
        } else {
            actor.setBounds((int) x + xoff, (int) y - 3, width, height);
        }
        mySurface inter = null;
        for (myIntersectable i : myWorld.inIntersectable) {
            /*
             * Loop through world objects and check for those intersectable.
             */
            if (!i.equals(this) && i instanceof mySurface) {
                /*
                 * Cast the intersectable object to paintable and create it's rectangle.
                 */
                myPaintable p = (myPaintable) i;
                Rectangle other = new Rectangle();
                other.setBounds((int) p.getX(), (int) p.getY(), p.getWidth(), p.getHeight());
                if (actor.intersects(other)) {
                    if (xoff == 0) {
                        /*
                         * Snap to the ground block and return true.
                         */
                        if (((mySurface) p).safe || invinsible) {
                            double othery = other.getY();
                            if (MAX_GRAVITY > 0) {
                                y = othery - height;
                            } else {
                                y = othery + height / 2;
                            }
                        } else {
                            die();
                            return null;
                        }
                    }
                    inter = (mySurface) i;
                }
            }
        }
        return inter;
    }

    /**
     * Determine whether or not the actor is on a spike.
     *
     * @return Whether or not the actor is on a spike.
     */
    private boolean hitSpike(int xoff) {
        if (dead) {
            return false;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle2D actor = new Rectangle();

        if (MAX_GRAVITY > 0) {
            actor.setRect((int) x + xoff, (int) y + 3, width, height);
        } else {
            actor.setRect((int) x + xoff, (int) y - 3, width, height);
        }

        try {
            for (myIntersectable i : myWorld.inIntersectable) {
                /*
                 * Loop through world objects and check for those intersectable.
                 */
                if (!i.equals(this) && i instanceof mySpike) {
                    /*
                     * Cast the intersectable object to paintable and create it's polygon.
                     */
                    myPaintable p = (myPaintable) i;
                    int[] xP = new int[]{(int) p.getX(), (int) p.getX() + p.getWidth() / 2, (int) p.getX() + p.getWidth()};
                    int[] yP = new int[]{(int) p.getY() + p.getHeight(), (int) p.getY(), (int) p.getY() + p.getHeight()};
                    Polygon other = new Polygon(xP, yP, 3);

                    if (other.intersects(actor)) {
                        if (xoff == 0) {
                            die();
                        }
                        return true;
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            die();
        }
        return false;
    }

    /**
     * Determine whether or not the actor is on a ground block.
     *
     * @return Whether or not the actor is on a ground block.
     */
    private boolean onGround(int xoff, int yoff) {
        if (dead) {
            return false;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle actor = new Rectangle();
        if (MAX_GRAVITY > 0) {
            actor.setBounds((int) x + xoff, (int) y + 3 + yoff, width, height);
        } else {
            actor.setBounds((int) x + xoff, (int) y - 3 + yoff, width, height);
        }

        for (myIntersectable i : myWorld.inIntersectable) {
            /*
             * Loop through world objects and check for those intersectable.
             */
            if (!i.equals(this) && i instanceof myGround) {
                /*
                 * Cast the intersectable object to paintable and create it's rectangle.
                 */
                myPaintable p = (myPaintable) i;
                Rectangle other = new Rectangle();
                other.setBounds((int) p.getX(), (int) p.getY(), p.getWidth(), p.getHeight());
                if (actor.intersects(other) && !((myGround) i).fake) {
                    if (xoff == 0 && yoff == 0) {
                        /*
                         * Snap to the ground block and return true.
                         */
                        double othery = other.getY();
                        if (y >= othery || (y <= othery && ys < 0)) {
                            die();
                            return false;
                        }
                        if (MAX_GRAVITY > 0) {
                            y = othery - height;
                        } else {
                            y = othery + height;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    //</editor-fold>

    @Override
    public BufferedImage paint() {
        return rotate(image, getRotation());
    }

    /**
     * Rotate an image using an AffineTransform.
     *
     * @param timg The image you want to rotate
     * @param degrees Degrees to rotate
     * @return The rotated image
     */
    public BufferedImage rotate(BufferedImage timg, double degrees) {
        AffineTransform xform = new AffineTransform();

        xform.setToTranslation(0.5 * timg.getWidth(), 0.5 * timg.getHeight());
        xform.rotate(degrees);
        xform.translate(-0.5 * timg.getHeight(), -0.5 * timg.getWidth());

        AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);

        return op.filter(timg, null);
    }

    /**
     * Get the rotation of the image.
     *
     * @return The rotation
     */
    private double getRotation() {
        return rotation;
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
        return ys;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private void die() {
        if (invinsible) {
            return;
        }
        dead = true;
        m.attempts++;
        levelManager.reset();
    }

    @Override
    public void addedToWorld(myWorld world) {
    }
}
