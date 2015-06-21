package Shootan.UI.EDITORInterface;


import java.awt.*;
import java.util.ArrayList;

public class Activity extends Control {

    private ArrayList<Control> controls=new ArrayList<>();


    public Activity(int x, int y, int width, int height, String brush) {
        super(x, y, width, height, null);
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.brush=brush;
    }

    public Activity addControl(Control c) {
        controls.add(c);
        return this;
    }

    public void draw(Graphics2D g2){
        for (Control c: controls) {
            c.draw(g2);
        }
    }

    public void processKeyBoardEvent(char ch) {
        for (Control c: controls) {
            c.processKeyBoardEvent(ch);
        }
    }

    public void processMousePress(int x, int y) {
        for (Control c: controls) {
            c.processMousePress(x, y, brush);
        }
    }

    protected void setBrush(String brush){
        this.brush=brush;
    }
}
