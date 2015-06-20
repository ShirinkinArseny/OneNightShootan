package Shootan.UI.EDITORInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class UICanvas extends Canvas {
    int sizeX=0, sizeY=0;
    Activity chosen;
    String typeOfBrush="Brick";


    public UICanvas() {
        super();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                char symb=e.getKeyChar();
                int code=e.getKeyCode();
                if(code!=10){
                    chosen.keyPressed(symb,code);
                }else{
                    try {
                        sizeX=Integer.valueOf(chosen.text[0].value);
                    }catch (NumberFormatException ex) {
                        System.err.println("Неверный формат первой строки, мать ее три раза");
                    }
                    try {
                        sizeY=Integer.valueOf(chosen.text[1].value);
                    }catch (NumberFormatException ex) {
                        System.err.println("Неверный формат второй строки, мать ее шесть раз");
                    }
                    if((sizeX!=0)&&(sizeY!=0)){
                        chosen=new Activity(1,0,0,0,getWidth(), getHeight(), sizeX,sizeY, g2);
                        chosen.buttons[0].setAction(new Runnable() {
                            @Override
                            public void run() {
                                typeOfBrush="Brick";
                            }
                        });
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x=e.getX(), y=e.getY();
                chosen.mouseDragged(x,y,typeOfBrush);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chosen.mouseClick(e.getX(),e.getY(),typeOfBrush);
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
        chosen=new Activity(1,2,0,0,getWidth(), getHeight(), g2);

        chosen.buttons[0].name="OK";
        chosen.buttons[0].setAction(new Runnable() {
            @Override
            public void run() {
                try {
                    sizeX=Integer.valueOf(chosen.text[0].value);
                }catch (NumberFormatException e) {
                    System.err.println("Неверный формат первой строки, мать ее три раза");
                }
                try {
                    sizeY=Integer.valueOf(chosen.text[1].value);
                }catch (NumberFormatException e) {
                    System.err.println("Неверный формат второй строки, мать ее шесть раз");
                }
                if((sizeX!=0)&&(sizeY!=0)){
                    chosen=new Activity(3,0,0,0,getWidth(), getHeight(), sizeX,sizeY, g2);
                    chosen.buttons[0].setAction(new Runnable() {
                        @Override
                        public void run() {
                            typeOfBrush="brick";
                            chosen.resetButtons();
                            chosen.setNewColor(0);
                        }
                    });
                    chosen.buttons[0].name="Brick";
                    chosen.buttons[1].setAction(new Runnable() {
                        @Override
                        public void run() {
                            typeOfBrush = "light";
                            chosen.resetButtons();
                            chosen.setNewColor(1);
                        }
                    });
                    chosen.buttons[1].name="Light";
                    chosen.buttons[2].setAction(new Runnable() {
                        @Override
                        public void run() {
                            typeOfBrush="erase";
                            chosen.resetButtons();
                            chosen.setNewColor(2);
                        }
                    });
                    chosen.buttons[2].name="Erase";
                }
            }
        });

        chosen.text[0]=new TextField(20,20);
        chosen.text[1]=new TextField(20,50);

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

        chosen.draw();

        bs.show();
    }
}
