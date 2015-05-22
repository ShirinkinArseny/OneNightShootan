package Shootan.UI.Render;

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

    public void draw(Graphics2D g2, int width, int height, StrangeWorld w) {

        float dx=width/2-w.getMe().getX()*blockSize;
        float dy=height/2-w.getMe().getY()*blockSize;

        int blockX=w.getMe().getBlockX();
        int blockY=w.getMe().getBlockY();

        for (int x=blockX-World.getPotentialViewDistance; x<=blockX+World.getPotentialViewDistance; x++) {

            for (int y=blockY-World.getPotentialViewDistance; y<=blockY+World.getPotentialViewDistance; y++) {

                if (w.isVisible(x, y)) {
                    g2.drawImage(
                            textureLoader.getBlockTexture(w.getBlock(x, y).getType()),
                            (int) (x * blockSize + dx),
                            (int) (y * blockSize + dy),
                            blockSize, blockSize, null);
                }

            }

        }


        for (Unit u: w.getUnits()) {
            g2.drawImage(
                    textureLoader.getUnitTexture(u.getType())[u.getAngle()],
                    (int)(u.getX()*blockSize+dx),
                    (int)(u.getY()*blockSize+dy),
                    blockSize, blockSize, null);
        }


    }

}
