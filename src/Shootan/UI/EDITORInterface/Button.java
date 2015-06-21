package Shootan.UI.EDITORInterface;

import java.awt.*;

public class Button extends Control {

    private Runnable onClickAction=null;
    private boolean isSelected=false;

    public Button(int x,int y, String text){
        super(x, y, 100, 20, text);
    }

    public void setAction(Runnable r){
        this.onClickAction=r;
    }

    public void processMousePress(int x, int y, String brush) {
        if ((x > this.x) && (x < this.x + width) && (y > this.y) && (y < this.y + height)) {
            isSelected=true;
            if (onClickAction!=null)
                onClickAction.run();
        }
    }

    public void draw(Graphics2D g2){
        super.draw(g2);
        if (isSelected) {
            g2.setColor(foregroundColor);
            g2.drawRect(x,y,width,height);
        }
    }

}
