/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hardlypossible;

import environment.Environment;
import image.ResourceTools;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.Player;

/**
 *
 * @author Jordan
 */
public class myWorld extends Environment {

    public static List<myPaintable> inPaintable = new ArrayList<>();
    public static List<myScrollable> inScrolling = new ArrayList<>();
    public static List<myActable> inActing = new ArrayList<>();
    public static List<myIntersectable> inIntersectable = new ArrayList<>();
    private static Set<String> keysDown = new HashSet<>();
    public final double SCROLLSPEED = 5.3;
    private final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private SoundThread t;
    public double scrollY;
    public int attempts;
    private boolean hasToReset = false;

    public myWorld(Image background) {
        super(background);
        startTimer(0, 10L);
    }

    public void reset() {
        hasToReset = true;
    }

    public void addObjectToWorld(Object obj) {
        if (obj instanceof myPaintable) {
            inPaintable.add((myPaintable) obj);
        }
        if (obj instanceof myActable) {
            inActing.add((myActable) obj);
        }
        if (obj instanceof myIntersectable) {
            inIntersectable.add((myIntersectable) obj);
        }
        if (obj instanceof myScrollable) {
            inScrolling.add((myScrollable) obj);
        }
    }

    @SuppressWarnings("deprecation")
    public void stopSound() {
        if (t != null && t.isAlive()) {
            t.stop();
        }
    }

    public void setSound(int track) {
        stopSound();
        t = new SoundThread(track);
        t.play();
    }

    @Override
    public void initializeEnvironment() {
        levelManager.initialize(this);

        // Change this to build other levels.
        levelManager.buildLevelOne();
    }

    public void setBackground(int background) {
        Image bg = null;
        switch (background) {
            case 1:
                bg = ResourceTools.loadImageFromResource("resources/images/background.jpg");
                break;
            default:
                try {
                    bg = ResourceTools.loadImageFromResource("resources/images/background" + background + ".jpg");
                    break;
                } catch (Exception e) {
                    setBackground(1);
                    return;
                }
        }
        if (bg == null) {
            return;
        }
        bg = bg.getScaledInstance(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height, 1);
        setBackground(bg);

    }

    @Override
    public void timerTaskHandler() {
        for (myScrollable a : inScrolling) {
            a.scroll(SCROLLSPEED, scrollY);
        }
        scrollY = 0;

        try {
            for (myActable a : inActing) {
                a.act();
            }
        } catch (ConcurrentModificationException e) {
        }
        if (hasToReset) {
            inPaintable.clear();
            inScrolling.clear();
            inActing.clear();
            inIntersectable.clear();
            stopSound();
            hasToReset = false;
            levelManager.buildCurrent();
        }
    }

    public boolean isKeyDown(String keyCompare) {
        if (keysDown == null) {
            return false;
        }
        if (keysDown.isEmpty()) {
            return false;
        }
        return keysDown.contains(keyCompare);
    }

    @Override
    public synchronized void keyPressedHandler(KeyEvent e) {
        if (getKeyText(e.getKeyCode()) == null) {
            return;
        }
        if (getKeyText(e.getKeyCode()) instanceof String) {
            keysDown.add(getKeyText(e.getKeyCode()).toLowerCase());
        } else {
            keysDown.add(getKeyText(e.getKeyCode()));
        }
        if (getKeyText(e.getKeyCode()).toLowerCase().equals("escape")) {
            System.exit(1);
        }
    }

    @Override
    public synchronized void keyReleasedHandler(KeyEvent e) {
        if (getKeyText(e.getKeyCode()) == null) {
            return;
        }
        if (getKeyText(e.getKeyCode()) instanceof String) {
            keysDown.remove(getKeyText(e.getKeyCode()).toLowerCase());
        } else {
            keysDown.remove(getKeyText(e.getKeyCode()));
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Key Manager">  
    static String getKeyText(int keyCode) {
        if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9 || keyCode >= KeyEvent.VK_A
                && keyCode <= KeyEvent.VK_Z) {
            return String.valueOf((char) keyCode);
        }
        switch (keyCode) {
            case KeyEvent.VK_COMMA:
                return "COMMA";
            case KeyEvent.VK_PERIOD:
                return "PERIOD";
            case KeyEvent.VK_SLASH:
                return "SLASH";
            case KeyEvent.VK_SEMICOLON:
                return "SEMICOLON";
            case KeyEvent.VK_EQUALS:
                return "EQUALS";
            case KeyEvent.VK_OPEN_BRACKET:
                return "OPEN_BRACKET";
            case KeyEvent.VK_BACK_SLASH:
                return "BACK_SLASH";
            case KeyEvent.VK_CLOSE_BRACKET:
                return "CLOSE_BRACKET";
            case KeyEvent.VK_ENTER:
                return "ENTER";
            case KeyEvent.VK_BACK_SPACE:
                return "BACK_SPACE";
            case KeyEvent.VK_TAB:
                return "TAB";
            case KeyEvent.VK_CANCEL:
                return "CANCEL";
            case KeyEvent.VK_CLEAR:
                return "CLEAR";
            case KeyEvent.VK_SHIFT:
                return "SHIFT";
            case KeyEvent.VK_CONTROL:
                return "CONTROL";
            case KeyEvent.VK_ALT:
                return "ALT";
            case KeyEvent.VK_PAUSE:
                return "PAUSE";
            case KeyEvent.VK_CAPS_LOCK:
                return "CAPS_LOCK";
            case KeyEvent.VK_ESCAPE:
                return "ESCAPE";
            case KeyEvent.VK_SPACE:
                return "SPACE";
            case KeyEvent.VK_PAGE_UP:
                return "PAGE_UP";
            case KeyEvent.VK_PAGE_DOWN:
                return "PAGE_DOWN";
            case KeyEvent.VK_END:
                return "END";
            case KeyEvent.VK_HOME:
                return "HOME";
            case KeyEvent.VK_LEFT:
                return "LEFT";
            case KeyEvent.VK_UP:
                return "UP";
            case KeyEvent.VK_RIGHT:
                return "RIGHT";
            case KeyEvent.VK_DOWN:
                return "DOWN";
            case KeyEvent.VK_MULTIPLY:
                return "MULTIPLY";
            case KeyEvent.VK_ADD:
                return "ADD";
            case KeyEvent.VK_SEPARATOR:
                return "SEPARATOR";
            case KeyEvent.VK_SUBTRACT:
                return "SUBTRACT";
            case KeyEvent.VK_DECIMAL:
                return "DECIMAL";
            case KeyEvent.VK_DIVIDE:
                return "DIVIDE";
            case KeyEvent.VK_DELETE:
                return "DELETE";
            case KeyEvent.VK_NUM_LOCK:
                return "NUM_LOCK";
            case KeyEvent.VK_SCROLL_LOCK:
                return "SCROLL_LOCK";
            case KeyEvent.VK_F1:
                return "F1";
            case KeyEvent.VK_F2:
                return "F2";
            case KeyEvent.VK_F3:
                return "F3";
            case KeyEvent.VK_F4:
                return "F4";
            case KeyEvent.VK_F5:
                return "F5";
            case KeyEvent.VK_F6:
                return "F6";
            case KeyEvent.VK_F7:
                return "F7";
            case KeyEvent.VK_F8:
                return "F8";
            case KeyEvent.VK_F9:
                return "F9";
            case KeyEvent.VK_F10:
                return "F10";
            case KeyEvent.VK_F11:
                return "F11";
            case KeyEvent.VK_F12:
                return "F12";
            case KeyEvent.VK_F13:
                return "F13";
            case KeyEvent.VK_F14:
                return "F14";
            case KeyEvent.VK_F15:
                return "F15";
            case KeyEvent.VK_F16:
                return "F16";
            case KeyEvent.VK_F17:
                return "F17";
            case KeyEvent.VK_F18:
                return "F18";
            case KeyEvent.VK_F19:
                return "F19";
            case KeyEvent.VK_F20:
                return "F20";
            case KeyEvent.VK_F21:
                return "F21";
            case KeyEvent.VK_F22:
                return "F22";
            case KeyEvent.VK_F23:
                return "F23";
            case KeyEvent.VK_F24:
                return "F24";
            case KeyEvent.VK_PRINTSCREEN:
                return "PRINTSCREEN";
            case KeyEvent.VK_INSERT:
                return "INSERT";
            case KeyEvent.VK_HELP:
                return "HELP";
            case KeyEvent.VK_META:
                return "META";
            case KeyEvent.VK_BACK_QUOTE:
                return "BACK_QUOTE";
            case KeyEvent.VK_QUOTE:
                return "QUOTE";
            case KeyEvent.VK_KP_UP:
                return "KP_UP";
            case KeyEvent.VK_KP_DOWN:
                return "KP_DOWN";
            case KeyEvent.VK_KP_LEFT:
                return "KP_LEFT";
            case KeyEvent.VK_KP_RIGHT:
                return "KP_RIGHT";
        }
        return null;
    }
//  </editor-fold>

    @Override
    public void environmentMouseClicked(MouseEvent e) {
    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        for (myPaintable p : inPaintable) {
            if (p.getX() + p.getWidth() > 0 && p.getX() + p.getWidth() < screenSize.width && p.getY() + p.getHeight() > 0 && p.getY() + p.getHeight() < screenSize.height) {
                graphics.drawImage(p.paint(), (int) p.getX(), (int) p.getY(), null);
            }
        }
    }

    public class SoundThread extends Thread {

        private String slug = "soundtrack";
        private Player p;

        public SoundThread(int level) {
            set(level);
        }

        public void set(int level) {
            switch (level) {
                case 1:
                    slug = "soundtrack";
                    break;
                default:
                    slug = "soundtrack" + level;
                    break;
            }
        }

        public void play() {
            start();
        }

        @Override
        public void run() {
            try {
                p = new Player(ResourceTools.getResourceAsStream("resources/sounds/" + slug + ".mp3"), new JavaSoundAudioDevice());
                p.play();
            } catch (JavaLayerException ex) {
                Logger.getLogger(myWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
