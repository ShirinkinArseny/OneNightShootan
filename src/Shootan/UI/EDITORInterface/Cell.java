package Shootan.UI.EDITORInterface;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Cell {
    int x,y,wX,wY,size; //x,y - положение в сетке; wX,wY - положение на экране
    char value;
    BufferedImage brick, light;

    public Cell(int x,int y, int leftB, int upB, int size, BufferedImage brick, BufferedImage light){
        this.size=size;
        this.x=x;
        this.y=y;
        wX=leftB+size*x;
        wY=upB+size*y;
        value='n';
        this.brick=brick;
        this.light=light;
    }

    protected void clickedL(char value){
        this.value=value;
    }

    protected void clickedR(){
        value='n';
    }

    protected void draw(Graphics2D g2){
        if(value=='b'){
            g2.drawImage(brick,wX,wY,wX+size,wY+size,0,0,128,128,null);
        }else{
            if(value=='l'){
                g2.drawImage(light,wX,wY,wX+size,wY+size,0,0,128,128,null);
            }
        }
    }
}
