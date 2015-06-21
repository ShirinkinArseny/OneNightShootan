package Shootan.UI.EDITORInterface;

import java.awt.*;

public abstract class Control {


    protected int x,y,width,height;
    protected String text, brush;
    protected static final Color backgroundColor=new Color(255,255,255);
    protected static final Color foregroundColor=new Color(0,0,0);

    public String getText() {
        return text;
    }

    public Control(int x,int y,int width,int height, String text){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.text=text;
    }

    public void draw(Graphics2D g2){
        g2.setColor(backgroundColor);
        g2.fillRect(x,y,width,height);
        g2.setColor(foregroundColor);
        g2.drawString(text,x+5,y+height/4*3);
    }

    public void processKeyBoardEvent(char c) {

    }

    public void processMousePress(int x, int y, String brush) {

    }

    public boolean getContainsPoint(int x, int y) {
        return (x > this.x) && (x < this.x + width) && (y > this.y) && (y < this.y + height);
    }




}
