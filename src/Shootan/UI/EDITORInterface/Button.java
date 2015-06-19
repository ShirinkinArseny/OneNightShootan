package Shootan.UI.EDITORInterface;


import java.awt.*;

public class Button {
    int x,y,width,height;
    Runnable action;
    String name;

    public Button(int x,int y,int width,int height, int number){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        name="button"+number;
    }

    protected void draw(Graphics2D g2){
        g2.setColor(new Color(255,255,255));
        g2.fillRect(x,y,width,height);
        g2.setColor(new Color(0,0,0));
        g2.drawString(name,x+5,y+height/4*3);
    }

    protected boolean clicked(int x, int y) {
            return ((x > this.x) && (x < this.x + width) && (y > this.y) && (y < this.y + height));
    }

    protected void setAction(Runnable r){
        this.action=r;
    }

    protected void dosmth(){
        action.run();
    }
}
