package Shootan.UI.Render;

import Shootan.Bullets.AbstractBullet;
import Shootan.Units.Unit;
import Shootan.Worlds.StrangeWorld;
import Shootan.Worlds.World;

import java.awt.*;

public class WorldRenderer {

    private TextureLoader textureLoader;

    public WorldRenderer() {
        textureLoader=new TextureLoader();
    }

    private int blockSize=100;

    public void setBlockSize(int size) {
        blockSize=size;
    }

    private void drawBlock(Graphics2D g2, int x, int y, StrangeWorld w, float dx, float dy) {
        int visibility=w.isVisible(x, y);
        if (visibility!=0) {
        g2.drawImage(
                textureLoader.getBlockTexture(w.getBlock(x, y).getType()),
                (int) (x * blockSize + dx),
                (int) (y * blockSize + dy),
                blockSize, blockSize, null);
        if (w.getBlock(x, y).hiLight) {
            g2.setColor(new Color(255, 128, 0));
            g2.drawRect(
                    (int) (x * blockSize + dx),
                    (int) (y * blockSize + dy),
                    blockSize, blockSize);
        }
        if (visibility == 1) {
            g2.setColor(new Color(0, 0, 0, 128));
            g2.fillRect(
                    (int) (x * blockSize + dx),
                    (int) (y * blockSize + dy),
                    blockSize, blockSize);
        }
        }
    }

    private void drawUnit(Graphics2D g2, StrangeWorld w, Unit u, float dx, float dy) {
            int diameter = (int) (blockSize * u.getRadius() * 2);
            g2.drawImage(
                    textureLoader.getUnitTexture(u.getType())[((int) (u.getViewAngle() * 360 / 2 / Math.PI))],
                    (int) ((u.getX() - u.getRadius()) * blockSize + dx),
                    (int) ((u.getY() - u.getRadius()) * blockSize + dy),
                    diameter, diameter, null);


            if (u.getHealth() > 0) {
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(1 - u.getHealth(), u.getHealth(), 0, 0.5f));
                g2.fillRect((int) ((u.getX() - u.getRadius()) * blockSize + dx),
                        (int) ((u.getY() - u.getRadius()) * blockSize + dy - 15),
                        (int) (diameter * u.getHealth()), 10);

                g2.setColor(new Color(1 - u.getHealth(), u.getHealth(), 0));
                g2.drawRect((int) ((u.getX() - u.getRadius()) * blockSize + dx),
                        (int) ((u.getY() - u.getRadius()) * blockSize + dy - 15),
                        (int) (diameter * u.getHealth()), 10);
                g2.drawRect((int) ((u.getX() - u.getRadius()) * blockSize + dx),
                        (int) ((u.getY() - u.getRadius()) * blockSize + dy - 15),
                        (int) (diameter), 10);
            }
    }

    private void drawBullet(Graphics2D g2, StrangeWorld w, AbstractBullet b, float dx, float dy, float time) {
        if (w.isVisible((int) b.getX(), (int) b.getY())!=0) {

            g2.drawLine(
                    (int) (b.getX() * blockSize + dx),
                    (int) (b.getY() * blockSize + dy),
                    (int) ((b.getX() + b.getDX() * time) * blockSize + dx),
                    (int) ((b.getY() + b.getDY() * time) * blockSize + dy));

        }
    }

    private long lastTimeNanos=System.nanoTime();
    public void draw(Graphics2D g2, int width, int height, StrangeWorld w) {

        long currentTimeNanos=System.nanoTime();
        float sec= (currentTimeNanos-lastTimeNanos)/1000000000.0f;
        lastTimeNanos=currentTimeNanos;

        float dx=width/2-w.getMe().getX()*blockSize;
        float dy=height/2-w.getMe().getY()*blockSize;

        int blockX= (int) w.getMe().getX();
        int blockY= (int) w.getMe().getY();

        g2.setStroke(new BasicStroke(7));
        for (int x=blockX-World.getPotentialViewDistance; x<=blockX+World.getPotentialViewDistance; x++) {
            for (int y=blockY-World.getPotentialViewDistance; y<=blockY+World.getPotentialViewDistance; y++) {
                drawBlock(g2, x, y, w, dx, dy);
            }
        }

        for (Unit u: w.getUnits()) {
            if (u.getId()!=w.getMe().getId()) {
                if (w.isVisible((int) u.getX(), (int) u.getY()) != 0) {
                    drawUnit(g2, w, u, dx, dy);
                }
            }
        }
        drawUnit(g2, w, w.getMe(), dx, dy);

        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(3));
        for (AbstractBullet b: w.getBullets()) {
            drawBullet(g2, w, b, dx, dy, sec);
        }


    }

}
