package Shootan.UI.EDITORInterface;

import java.awt.*;

public class TextField {
    int x,y,width,height;
    String value;
    boolean chosen=false;

    public TextField(int x, int y){
        this.x=x;
        this.y=y;
        width=100;
        height=20;
        value="";
    }

    protected boolean clicked(int x, int y) {
        return ((x > this.x) && (x < this.x + width) && (y > this.y) && (y < this.y + height));
    }

    protected void draw(Graphics2D g2){
        g2.setColor(new Color(0, 0, 0));
        g2.drawRect(x, y, width, height);
        g2.drawString(value,x+5,y+height/4*3);
    }


}
