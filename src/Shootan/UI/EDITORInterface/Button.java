package Shootan.UI.EDITORInterface;


import java.awt.*;
import java.util.Random;

public class Button {
    int x,y,width,height,c1=10,c2=10,c3=10;
    Graphics2D g2;
    Runnable action;

    public Button(int x,int y,int width,int height, Graphics2D g2){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.g2=g2;
    }

    protected void draw(){
        g2.setColor(new Color(c1,c2,c3));
        g2.fillRect(x,y,width,height);
    }

    protected boolean clicked(int x, int y) {
        System.out.println(x+"; "+y);
            return ((x > this.x) && (x < this.x + width) && (y > this.y) && (y < this.y + height));
    }

    protected void setAction(Runnable r){
        this.action=r;
    }

    protected void dosmth(){
        action.run();
    }
}
