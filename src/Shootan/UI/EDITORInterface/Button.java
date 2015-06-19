package Shootan.UI.EDITORInterface;


import java.awt.*;

public class Button {
    int x,y,width,height;
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
        g2.setColor(new Color(255,255,255));
        g2.fillRect(x,y,x+width,y+height);
    }

    protected void dosmth(Runnable r){
        this.action=r;
    }
}
