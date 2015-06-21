package Shootan.UI.EDITORInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class UICanvas extends Canvas {
    int sizeX=0, sizeY=0;
    Activity currentActivity;


    public UICanvas() {
        super();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                currentActivity.processKeyBoardEvent(e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                currentActivity.processMousePress(e.getX(), e.getY());
                currentActivity.processMouseDrag(e.getX(),e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentActivity.processMousePress(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) { currentActivity.processMousePressed(e.getX(), e.getY()); }

            @Override
            public void mouseReleased(MouseEvent e) { currentActivity.processMouseRelease(); }


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
                //уменьшение: 1, увеличение: -1
                currentActivity.processMouseWheel(e.getX(), e.getY(),e.getWheelRotation());
            }
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

        currentActivity =new Activity(0,0,getWidth(), getHeight(), "");
        TextField mapWidth=new TextField(0, 0, "Map width");
        TextField mapHeight=new TextField(0, 25, "Map height");
        Button acceptMapSize=new Button(0, 50, "Accept size");

        currentActivity.addControl(mapWidth).addControl(mapHeight).addControl(acceptMapSize);

        acceptMapSize.setAction(() -> {

            if (!mapWidth.getText().matches("[0-9]+") || !mapHeight.getText().matches("[0-9]+")) {
                new Exception("Неверный формат строки, мать ее N раз").printStackTrace();
            } else {

                sizeX = Integer.valueOf(mapWidth.getText());
                sizeY = Integer.valueOf(mapHeight.getText());

                currentActivity = new Activity(0, 0, getWidth(), getHeight(), "brick");

                Button handButton=new Button(0, 0, "Hand");
                handButton.setAction(() -> currentActivity.setBrush("hand"));

                Button brickButton=new Button(0, 25, "Brick");
                brickButton.setAction(() -> currentActivity.setBrush("brick"));

                Button lightButton=new Button(0, 50, "Light");
                lightButton.setAction(() -> currentActivity.setBrush("light"));

                Button floorButton=new Button(0, 75, "Floor");
                floorButton.setAction(() -> currentActivity.setBrush("floor"));



                BlockField field=new BlockField(sizeX,sizeY,getWidth(),getHeight());

                currentActivity.addControl(handButton).addControl(brickButton).addControl(lightButton).addControl(floorButton).addControl(field);
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

        currentActivity.draw(g2);

        bs.show();
    }
}
