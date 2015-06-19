package Shootan.UI.EDITORInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class UICanvas extends Canvas {
    Button buttons[]=new Button[20];
    int kOfButtons;
    public UICanvas() {
        super();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(int i=0;i<kOfButtons;i++)
                if(buttons[i].clicked(e.getX(),e.getY())){
                    buttons[i].dosmth();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addMouseWheelListener(e -> {
        });

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {

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
        kOfButtons=2;
        for(int i=0;i<kOfButtons;i++){
            buttons[i]=new Button(5,100+i*20,100,20,i,g2);
        }
        buttons[0].setAction(new Runnable() {
            @Override
            public void run() {
                buttons[0].c1 += 10;
                buttons[0].c2 += 10;
                buttons[0].c3 += 10;
            }
        });
        buttons[1].setAction(new Runnable() {
            @Override
            public void run() {
                buttons[1].c1 += 10;
                buttons[1].c2 += 10;
                buttons[1].c3 += 10;
            }
        });

        new Timer(20, e -> {
            update();
            draw();
        }).start();
    }

    private BufferStrategy bs;
    private Graphics2D g2;

    private void update() {



    }

    private void draw() {
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(new Color(180, 180, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        /*
        YOUR FUKKEN RENDER
         */
        for(int i=0;i<kOfButtons;i++){
            buttons[i].draw();
        }


        bs.show();
    }
}
