package Shootan.UI;

import Shootan.Units.Human;
import Shootan.Worlds.StrangeWorld;
import Shootan.Worlds.World;
import Shootan.UI.Render.WorldRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class UICanvas extends Canvas {

    private boolean[] keys=new boolean[256];

    private void mouseMovedOrDragged(int newx, int newy) {

        int dx=newx-getWidth()/2;
        int dy=newy-getHeight()/2;
        float angle=0;
        if (dx!=0 || dy!=0) {
            angle= (float) Math.atan2(dy, dx);
        }
        world.getMe().setViewAngle(angle);
    }

    public UICanvas() {
        super();

        for (int i=0; i<256; i++) {
            keys[i]=false;
        }

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()<256) {
                    keys[e.getKeyCode()] = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()<256) {
                keys[e.getKeyCode()]=false;
                }
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseMovedOrDragged(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseMovedOrDragged(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                world.getMe().getWeapon().setWannaShot(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                world.getMe().getWeapon().setWannaShot(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

            }
        });

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {

                try {
                    bs = getBufferStrategy();
                    g2 = (Graphics2D) bs.getDrawGraphics();
                    renderer.setBlockSize(Math.max(getWidth(), getHeight())*3/2/World.getPotentialViewDistance);
                } catch (NullPointerException ex) {
                    //ignore
                }

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

    }


    public void start() {
        createBufferStrategy(3);
        bs = getBufferStrategy();
        g2 = (Graphics2D) bs.getDrawGraphics();
        new Timer(20, e -> {
            update();
            draw();
        }).start();
    }

    private BufferStrategy bs;
    private Graphics2D g2;

    private StrangeWorld world=new StrangeWorld(new Human(10, 10));
    private WorldRenderer renderer=new WorldRenderer();

    private long lastTimeNanos=System.nanoTime();
    private float summaryTime=0;
    private int frames=0;

    private void update() {

        long currentTimeNanos=System.nanoTime();
        float sec= (currentTimeNanos-lastTimeNanos)/1000000000.0f;
        lastTimeNanos=currentTimeNanos;

        summaryTime+=sec;
        frames++;
        if (frames==100) {

            System.out.println("FPS: "+100/summaryTime);
            frames=0;
            summaryTime=0;
        }



        /*

        STUPID INPUT

         */

        boolean up=false;
        boolean down=false;
        boolean left=false;
        boolean right=false;

        if (keys[KeyEvent.VK_W]) {
            up=true;
        }
        if (keys[KeyEvent.VK_A]) {
            left=true;
        }
        if (keys[KeyEvent.VK_S]) {
            down=true;
        }
        if (keys[KeyEvent.VK_D]) {
            right=true;
        }

        if (up && down) {
            up=false;
            down=false;
        }

        if (left && right) {
            left=false;
            right=false;
        }

        double angle=-1;

        if (up) {
            if (left) {
                angle=Math.PI*5/4;
            } else
            if (right) {
                angle=Math.PI*7/4;
            } else
                angle=Math.PI*6/4;
        } else
        if (down) {
            if (left) {
                angle=Math.PI*3/4;
            } else
            if (right) {
                angle=Math.PI*1/4;
            } else
                angle=Math.PI*2/4;
        } else
            if (left) {
                angle=Math.PI*4/4;
            } else
            if (right) {
                angle=0;
            }

        if (angle >= 0) {
            world.getMe().setMotionAngle((float) angle);
            world.getMe().setIsMoving(true);
        } else {
            world.getMe().setIsMoving(false);
        }



        world.update(sec);



    }

    private void draw() {
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0));
        g2.fillRect(0, 0, getWidth(), getHeight());


        renderer.draw(g2, getWidth(), getHeight(), world);



        bs.show();
    }
}
