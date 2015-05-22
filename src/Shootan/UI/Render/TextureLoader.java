package Shootan.UI.Render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextureLoader {

    private BufferedImage[] human;

    private BufferedImage brick;
    private BufferedImage floor;

    private BufferedImage[] makeRotatedBitmaps(BufferedImage img) {


        BufferedImage[] imgs=new BufferedImage[8];
        for (int i=0; i<8; i++) {
            imgs[i]=new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2= (Graphics2D) imgs[i].getGraphics();

            g2.rotate(i*Math.PI/4, img.getWidth()/2, img.getHeight()/2);

            g2.drawImage(img,
                    0, 0,
                    null
            );

            g2.dispose();
        }
        return imgs;

    }

    public TextureLoader() {

        try {



            human = makeRotatedBitmaps(ImageIO.read(this.getClass().getResourceAsStream("human.png")));
            brick = ImageIO.read(this.getClass().getResourceAsStream("brick.png"));
            floor = ImageIO.read(this.getClass().getResourceAsStream("floor.png"));


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }



    }

    public BufferedImage[] getUnitTexture(long type) {
        return human;
    }

    public BufferedImage getBlockTexture(long type) {
            if (type==0) return brick;
            if (type==1) return floor;
        return null;
    }

}
