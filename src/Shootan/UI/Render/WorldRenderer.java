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

    public void draw(Graphics2D g2, int width, int height, StrangeWorld w) {

        float dx=width/2-w.getMe().getX()*20;
        float dy=height/2-w.getMe().getY()*20;

        int blockX=w.getMe().getBlockX();
        int blockY=w.getMe().getBlockY();

        for (int x=blockX-World.getPotentialViewDistance; x<=blockX+World.getPotentialViewDistance; x++) {

            for (int y=blockY-World.getPotentialViewDistance; y<=blockY+World.getPotentialViewDistance; y++) {

                if (w.isVisible(x, y)) {
                    g2.drawImage(
                            textureLoader.getBlockTexture(w.getBlock(x, y).getType()),
                            (int) (x * 20 + dx),
                            (int) (y * 20 + dy),
                            20, 20, null);
                }

            }

        }


        for (Unit u: w.getUnits()) {
            g2.drawImage(
                    textureLoader.getUnitTexture(u.getType())[u.getAngle()],
                    (int)(u.getX()*20+dx),
                    (int)(u.getY()*20+dy),
                    20, 20, null);
        }


    }

}
