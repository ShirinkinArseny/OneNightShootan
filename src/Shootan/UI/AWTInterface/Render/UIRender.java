package Shootan.UI.AWTInterface.Render;

import Shootan.GameEssences.Blocks.Block;
import Shootan.Worlds.ClientWorld;
import Shootan.Worlds.AbstractWorld;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UIRender {

    private ArrayList<String> messages=new ArrayList<>();

    private BufferedImage map=new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public void addMessage(String s) {
        messages.add(s);
        if (messages.size()>7)
            messages.remove(0);
    }

    public void draw(Graphics2D g2, int width, int height, ClientWorld w) {

        g2.setStroke(new BasicStroke(1));

        int timeWidth= (int) (w.getMe().getWeapon().getTimeToNextShot()*100);
        g2.setColor(new Color(255, 128, 0, 128));
        g2.fillRect(10, 10, timeWidth, 20);
        g2.setColor(new Color(255, 128, 0));
        g2.drawRect(10, 10, timeWidth, 20);
        g2.drawRect(10, 10, 100, 20);
        g2.drawString(w.getMe().getWeapon().getName(), 20, 25);

        g2.setColor(new Color(255, 128, 0, 128));
        g2.fillRect(10, 40, 100, 20);
        g2.setColor(new Color(255, 128, 0));
        g2.drawRect(10, 40, 100, 20);
        int inCage=w.getMe().getWeapon().getHaveBulletsInCage();
        int total=w.getMe().getWeapon().getHaveBullets();
        g2.drawString(inCage+"/"+(total-inCage), 20, 55);

        int y=height-20;
        for (int i=messages.size()-1; i>=0; i--) {

            int stringWidth=g2.getFontMetrics().stringWidth(messages.get(i))+10;

            g2.setColor(new Color(255, 128, 0, 128));
            g2.fillRect(15, y-12, stringWidth, 17);
            g2.setColor(new Color(255, 128, 0));
            g2.drawRect(15, y-12, stringWidth, 17);

            g2.setColor(new Color(0, 0, 0));
            g2.drawString(messages.get(i), 20, y);

            y-=20;
        }

        g2.setColor(new Color(255, 128, 0, 128));
        g2.fillRect(width-map.getWidth()-15, 5, map.getWidth()+10, map.getHeight()+10);
        g2.drawImage(map, width-map.getWidth()-10, 10, null);
        g2.setColor(new Color(255, 128, 0));
        g2.drawRect(width - map.getWidth() - 15, 5, map.getWidth() + 10, map.getHeight() + 10);


    }

    public void updateMap(ClientWorld world) {

        BufferedImage map=new BufferedImage(AbstractWorld.potentialViewDistance *2, AbstractWorld.potentialViewDistance *2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2= (Graphics2D) map.getGraphics();

        int x=1;
        for (int i= (int) (world.getMe().getX()- AbstractWorld.potentialViewDistance); i<world.getMe().getX()+ AbstractWorld.potentialViewDistance; i++) {

            int y=1;

            for (int j= (int) (world.getMe().getY()- AbstractWorld.potentialViewDistance); j<world.getMe().getY()+ AbstractWorld.potentialViewDistance; j++) {

                Block b=world.getBlock(i, j);
                int visibility=(b==null || b.getIsHard())?255:0;


                g2.setColor(new Color(0, 0, 0, visibility));
                g2.fillRect(x, y, 1, 1);

                y++;
            }
            x++;
        }


        g2.setColor(new Color(255, 128, 0));
        g2.fillOval(map.getWidth() / 2 - 2, map.getHeight() / 2 - 2, 4, 4);

        g2.dispose();


        this.map=map;

    }
}
