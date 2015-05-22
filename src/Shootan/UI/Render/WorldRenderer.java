package Shootan.UI.Render;

import Shootan.Units.Unit;
import Shootan.Worlds.World;

import java.awt.*;

public class WorldRenderer {

    private TextureLoader textureLoader;

    public WorldRenderer() {
        textureLoader=new TextureLoader();
    }

    public void draw(Graphics2D g2, World w) {

        for (Unit u: w.getUnits()) {
            g2.drawImage(
                    textureLoader.getUnitTexture(u.getType())[u.getAbsoluteAngle()],
                    (int)(u.getX()*20),
                    (int)(u.getY()*20),
                    20, 20, null);
        }


    }

}
