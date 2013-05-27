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
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.PlaybackEvent;

/**
 *
 * @author Jordan
 */
public class myWorld extends Environment {

    boolean inLevelView = true;
    int levelFactor = 3;
    public int scrollPosition;
    public static List<myPaintable> inPaintable = new ArrayList<>();
    public static List<myScrollable> inScrolling = new ArrayList<>();
    public static List<myActable> inActing = new ArrayList<>();
    public static List<myIntersectable> inIntersectable = new ArrayList<>();
    private static Set<String> keysDown = new HashSet<>();
    public final double SCROLLSPEED = 5.3;
    private final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public JSound jsound;
    public double scrollY;
    public int attempts;
    public boolean built;
    private boolean hasToReset = false;
    boolean building = false;

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
            ((myActable) obj).addedToWorld(this);
        }
        if (obj instanceof myIntersectable) {
            inIntersectable.add((myIntersectable) obj);
        }
        if (obj instanceof myScrollable) {
            inScrolling.add((myScrollable) obj);
        }
    }

    public void removeObjectFromWorld(Object obj) {
        if (obj instanceof myPaintable && inPaintable.contains((myPaintable) obj)) {
            inPaintable.remove((myPaintable) obj);
        }
        if (obj instanceof myActable && inActing.contains((myActable) obj)) {
            inActing.remove((myActable) obj);
        }
        if (obj instanceof myIntersectable && inIntersectable.contains((myIntersectable) obj)) {
            inIntersectable.remove((myIntersectable) obj);
        }
        if (obj instanceof myScrollable && inScrolling.contains((myScrollable) obj)) {
            inScrolling.remove((myScrollable) obj);
        }
    }

    public void setSound(int track) {
        if (jsound != null) {
            jsound.pause();
        }
        jsound = new JSound(track);

        jsound.play();
    }

    @Override
    public void initializeEnvironment() {
        levelManager.initialize(this);

        // Change this to build other levels.
        levelManager.buildCurrent();
    }

    public void setBackground(int background) {
        Image bg;
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
        if (!built) {
            return;
        }
        try {
            scrollPosition += SCROLLSPEED;
            for (myScrollable a : inScrolling) {
                if (!built) {
                    return;
                }
                a.scroll(SCROLLSPEED, scrollY);
            }

            scrollY = 0;

            for (myActable a : inActing) {
                if (!built) {
                    return;
                }
                a.act();
            }
        } catch (ConcurrentModificationException e) {
        }
        if (hasToReset) {
            inPaintable.clear();
            inScrolling.clear();
            inActing.clear();
            inIntersectable.clear();
            for (MouseListener ml : this.getMouseListeners()) {
                this.removeMouseListener(ml);
            }
            jsound.pause();
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
        if (isKeyDown("escape") && levelManager.current > 0) {
            levelManager.toggle();
        }
        if (isKeyDown("q") || (isKeyDown("escape") && isKeyDown("shift"))) {
            System.exit(1);
        }
        if (isKeyDown("p") && levelManager.current > 0) {
            inLevelView = !inLevelView;
        }
        if (isKeyDown("m") && levelManager.current > 0) {
            levelManager.reset();
            levelManager.set(0);
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
        try {
            for (myPaintable p : inPaintable) {
                if (p.getX() + p.getWidth() > 0 && p.getX() + p.getWidth() < screenSize.width && p.getY() + p.getHeight() > 0 && p.getY() + p.getHeight() < screenSize.height) {
                    if (!inLevelView) {
                        BufferedImage img = p.paint();
                        graphics.drawImage(img.getScaledInstance(img.getWidth() / levelFactor, img.getHeight() / levelFactor, 1), (int) p.getX(), (int) p.getY(), null);
                    } else {
                        graphics.drawImage(p.paint(), (int) p.getX(), (int) p.getY(), null);
                    }
                }
            }
        } catch (ConcurrentModificationException exc) {
        }
    }

    public class JSound extends JPlayer.PlaybackListener implements Runnable {

        private String filePath, slug = "soundthread";
        private JPlayer player;
        private Thread playerThread;

        private void set(int level) {
            switch (level) {
                case 0:
                    slug = "menutrack";
                    break;
                case 1:
                    slug = "soundtrack";
                    break;
                default:
                    slug = "soundtrack" + level;
                    break;
            }
        }

        public JSound(int level) {
            set(level);
            this.filePath = "src/resources/sounds/" + slug + ".mp3";
        }

        public void pause() {
            this.player.pause();

            try {
                this.playerThread.stop();
            } catch (Exception e) {
            }
            this.playerThread = null;
        }

        public void pauseToggle() {
            if (this.player.isPaused == true) {
                this.play();
            } else {
                this.pause();
            }
        }

        public void play() {
            if (this.player == null) {
                this.playerInitialize();
            }

            this.playerThread = new Thread(this, "AudioPlayerThread");

            this.playerThread.start();
        }

        private void playerInitialize() {
            try {
                String urlAsString =
                        "file:///"
                        + new java.io.File(".").getCanonicalPath()
                        + "/"
                        + this.filePath;

                this.player = new JPlayer(
                        new URL(urlAsString),
                        this);
            } catch (IOException | JavaLayerException ex) {
            }
        }

        // PlaybackListener members
        public void playbackStarted(PlaybackEvent playbackEvent) {
        }

        public void playbackFinished(PlaybackEvent playbackEvent) {
        }

        @Override
        public void run() {
            try {
                this.player.resume();
            } catch (JavaLayerException ex) {
            }
        }
    }
}
