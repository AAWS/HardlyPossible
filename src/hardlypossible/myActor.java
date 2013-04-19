/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import java.awt.Color;
import java.awt.Graphics;
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
    private final double GRAVITY = 0.4, MAX_GRAVITY = 8.9, JUMPSTR = 8.8, ROTATIONSPEED = 0.055, TOP_SCREEN = 650, BOTTOM_SCREEN = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300;
    private BufferedImage image;
    private boolean dead;

    public myActor(myWorld m, double x, double y) {
        this.m = m;
        this.x = x - 3;
        this.y = y;
        /*
         * Cache the image.
         */
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(224, 67, 26));
        g.fillRect(1, 1, width - 2, height - 2);
    }

    public myActor(myWorld world) {
        this.m = world;
        this.x = 100;
        this.y = BOTTOM_SCREEN - 50;
        /*
         * Cache the image.
         */
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(224, 67, 26));
        g.fillRect(1, 1, width - 2, height - 2);
    }

    @Override
    public void act() {
        if (!onGround() && !onBlock()) {
            /*
             * Not on ground so add gravity and rotation.
             */
            if (ys <= MAX_GRAVITY) {
                ys += GRAVITY;
            }
            rotation += ROTATIONSPEED;
        } else if (onGround() || onBlock()) {
            /*
             * On ground so zero rotation and vertical velocity.
             */
            rotation = 0;
            ys = 0;
        }
        /*
         * Jump.
         */
        if (m.isKeyDown("space") && (onGround() || onBlock())) {
            ys = -JUMPSTR;
        }

        hitSpike();
        if (dead) {
            return;
        }

        /*
         * Move.
         */
        y += ys;
        if (y <= TOP_SCREEN) {
            m.scrollY = -m.SCROLLSPEED / 2;
        } else if (y >= BOTTOM_SCREEN) {
            m.scrollY = m.SCROLLSPEED / 2;
        }
    }

    /**
     * Determine whether or not the actor is on a ground block.
     *
     * @return Whether or not the actor is on a ground block.
     */
    private boolean onGround() {
        if (dead) {
            return false;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle actor = new Rectangle();
        actor.setBounds((int) x, (int) y + 3, width, height);
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
                if (actor.intersects(other)) {
                    /*
                     * Snap to the ground block and return true.
                     */
                    double othery = other.getY();
                    if (y >= othery || (y <= othery && ys < 0)) {
                        die();
                        return false;
                    }
                    y = othery - height;
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
    private boolean onBlock() {
        if (dead) {
            return false;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle actor = new Rectangle();
        actor.setBounds((int) x, (int) y + 3, width, height);
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
                    /*
                     * Snap to the ground block and return true.
                     */
                    if (((mySurface) p).safe) {
                        double othery = other.getY();
                        y = othery - height;
                        return true;
                    } else {
                        die();
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determine whether or not the actor is on a spike.
     *
     * @return Whether or not the actor is on a spike.
     */
    private void hitSpike() {
        if (dead) {
            return;
        }
        /*
         * Create a rectangle for comparisons.
         */
        Rectangle2D actor = new Rectangle();
        actor.setRect((int) x, (int) y + 3, width, height);

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
                        die();
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            die();
        }
    }

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
        dead = true;
        m.attempts++;
        levelManager.reset();
    }
}
