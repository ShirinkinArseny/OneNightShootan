package Shootan.UI.EDITORInterface;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Block {
    int x,y,size=100;
    BufferedImage bi;



    public Block(int x, int y){
        this.x=x;
        this.y=y;
        try {
            bi= ImageIO.read(new File("content/brick.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    protected void draw(Graphics2D g2){
        g2.drawImage(bi,x,y,x+size,y+size,0,0,128,128,null);
    }

    protected boolean cross(int x, int y){
        return ((x<this.x+size)&&(x>this.x-size)&&(y<this.y+size)&&(y>this.y-size));
    }

}
