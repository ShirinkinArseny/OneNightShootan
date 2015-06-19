package Shootan.UI.EDITORInterface;

import org.lwjgl.Sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class UICanvas extends Canvas {
    int sizex,sizey;
    Button buttons[]=new Button[20];
    Block blocks[]=new Block[1024];
    TextField width,height;
    int kOfButtons, kOfBlocks=0, sizeX=0, sizeY=0;

    public UICanvas() {
        super();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                char symb=e.getKeyChar();
                if((symb>='0')&&(symb<='9'))
                if((sizeY==0)||(sizeX==0)){
                    if(width.chosen){
                        width.value+=symb;
                    }
                    if(height.chosen){
                        height.value+=symb;
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
                boolean cross=false;
                for(int i=0;i<kOfBlocks;i++){
                    if (!cross){
                        cross=blocks[i].cross(x,y);
                    }
                }
                if (!cross){
                    kOfBlocks+=1;
                    blocks[kOfBlocks-1]=new Block(x,y);
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x=e.getX(), y=e.getY();
                for(int i=0;i<kOfButtons;i++)
                if(buttons[i].clicked(x,y)) {
                    buttons[i].dosmth();
                }
                boolean cross=false;
                for(int i=0;i<kOfBlocks;i++){
                    if (!cross){
                        cross=blocks[i].cross(x,y);
                    }
                }
                if (!cross){
                    kOfBlocks+=1;
                    blocks[kOfBlocks-1]=new Block(x,y);
                }
                if((sizeX==0)||(sizeY==0)){
                    width.chosen=width.clicked(x,y);
                    height.chosen=height.clicked(x,y);
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
            buttons[i]=new Button(20,80+i*20,100,20,i);
        }
        buttons[0].name="OK";
        buttons[0].setAction(new Runnable() {
            @Override
            public void run() {
                sizeX=Integer.valueOf(width.value);
                sizeY=Integer.valueOf(height.value);
            }
        });

        width=new TextField(20,20);
        height=new TextField(20,50);
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
        if((sizeX==0)||(sizeY==0)) {
            width.draw(g2);
            height.draw(g2);
            buttons[0].draw(g2);
        }else {


            for (int i = 0; i < kOfButtons; i++) {
                buttons[i].draw(g2);
            }

            for (int i = 0; i < kOfBlocks; i++) {
                blocks[i].draw(g2);
            }
        }

        bs.show();
    }
}
