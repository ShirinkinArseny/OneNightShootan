package Shootan.UI.EDITORInterface;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BlockField {
    int width,height,windowX,windowY,leftB=400,upB=1,size;
    Cell[][] blocks;/** 'n' - пустая клетка, 'b' - блок **/
    BufferedImage brick, light;

    public BlockField(int x, int y, int wX, int wY){
        width=x;
        height=y;
        windowX=wX;
        windowY=wY;
        size=Math.min((windowX-leftB)/width,(windowY-upB)/height);
        try {
            brick= ImageIO.read(new File("content/brick.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            light= ImageIO.read(new File("content/flame.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        blocks=new Cell[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                blocks[i][j]=new Cell(j,i,leftB,upB,size,brick,light);
            }
        }
    }

    protected void draw(Graphics2D g2){
        for(int i=0;i<=height;i++){
            g2.drawLine(leftB,upB+size*i,leftB+size*width,upB+size*i);
        }
        for(int i=0;i<=width;i++){
            g2.drawLine(leftB+size*i,upB,leftB+size*i,upB+size*height);
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                blocks[i][j].draw(g2);
            }
        }
    }

    protected void mouseClick(int x, int y, String brush){
        if((x>=leftB)&&(x<=leftB+size*width)&&(y>=upB)&&(y<=upB+size*height)){
            switch (brush){
                case "erase":
                    if(blocks[(y-upB)/size][(x-leftB)/size].value!='n'){
                        blocks[(y-upB)/size][(x-leftB)/size].clickedR();
                    }
                    break;
                case "brick":
                    if(blocks[(y-upB)/size][(x-leftB)/size].value!='b'){
                        blocks[(y-upB)/size][(x-leftB)/size].clickedL('b');
                    }
                    break;
                case "light":
                    if(blocks[(y-upB)/size][(x-leftB)/size].value!='l'){
                        blocks[(y-upB)/size][(x-leftB)/size].clickedL('l');
                    }
            }
        }
    }


}
