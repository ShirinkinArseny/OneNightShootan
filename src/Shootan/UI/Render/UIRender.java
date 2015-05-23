package Shootan.UI.Render;

import Shootan.Worlds.StrangeWorld;

import java.awt.*;

public class UIRender {

    public void draw(Graphics2D g2, int width, int height, StrangeWorld w) {


        int timeWidth= (int) (w.getMe().getWeapon().getTimeToNextShot()*100);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(255, 128, 0, 128));
        g2.fillRect(10, 10, timeWidth, 20);
        g2.setColor(new Color(255, 128, 0));
        g2.drawRect(10, 10, timeWidth, 20);
        g2.drawRect(10, 10, 100, 20);

    }

}
