package Shootan.UI.EDITORInterface;

import org.lwjgl.Sys;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cell {
    int x,y,wX,wY,size; //x,y - положение в сетке; wX,wY - положение на экране
    char value;
    BufferedImage texture;

    public Cell(int x,int y, int leftB, int upB, int size, BufferedImage bi){
        this.size=size;
        this.x=x;
        this.y=y;
        wX=leftB+size*x;
        wY=upB+size*y;
        value='n';
        texture=bi;
    }

    protected void clickedL(){
        value='b';
    }

    protected void clickedR(){
        value='n';
    }

    protected void draw(Graphics2D g2){
        if(value=='b'){
            g2.drawImage(texture,wX,wY,wX+size,wY+size,0,0,128,128,null);
        }
    }
}
