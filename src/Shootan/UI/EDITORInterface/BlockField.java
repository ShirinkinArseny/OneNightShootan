package Shootan.UI.EDITORInterface;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BlockField extends Control{
    int width,height,leftBR=400,upBR=1,sizeR,winX,winY,lx=-1,ly=-1; //leftBR/upBR - реальные значения границ, leftB/upB изменяем во время зума
    Cell[][] blocks;/** 'n' - пустая клетка, 'b' - блок **/
    BufferedImage brick, light;
    double leftB=400,upB=1, k=1, size;

    public BlockField(int x, int y, int wX, int wY){
        super(400,wY-1,x*Math.min((wX-400)/x,(wY-1)/y),y*Math.min((wX-400)/x,(wY-1)/y),"");
        size=Math.min((wX-leftBR)/x,(wY-upBR)/y);
        sizeR=(int)size;
        System.out.println(size);
        width=x;
        height=y;
        winX=wX;
        winY=wY;
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
                blocks[i][j]=new Cell(j,i,leftBR,upBR,(int)size,brick,light);
            }
        }
    }

    public void draw(Graphics2D g2){
        super.draw(g2);
        //System.out.println("y: "+(int)((upBR-upB)/size)+"; "+(int)((upBR+sizeR*height-upB)/size));
        //System.out.println("x: "+(int)((leftBR-leftB)/size)+"; "+(int)((leftBR+sizeR*width-leftB)/size));
        int maxY,maxX,minX,minY;
        if(((int)((upBR+sizeR*height-upB)/size)>height)||(((int)((upBR+sizeR*height-upB)/size)<0))){
            maxY=height;
        }else{
            maxY=(int)((upBR+sizeR*height-upB)/size);
        }
        if(((int)((leftBR+sizeR*width-leftB)/size)>width)||(((int)((leftBR+sizeR*width-leftB)/size)<0))){
            maxX=width;
        }else{
            maxX=(int)((leftBR+sizeR*width-leftB)/size);
        }
        if((int)((upBR-upB)/size)<0){
            minY=0;
        }else{
            minY=(int)((upBR-upB)/size);
        }
        if((int)((leftBR-leftB)/size)<0){
            minX=0;
        }else{
            minX=(int)((leftBR-leftB)/size);
        }

        for(int i=minY;i<maxY;i++){
            for(int j=minX;j<maxX;j++){
                blocks[i][j].draw(g2);
            }
        }
    }

    public void processMousePress(int x, int y, String brush){
        if((x>=leftB)&&(x<=leftB+size*width)&&(y>=upB)&&(y<=upB+size*height)){
            switch (brush){
                case "erase":
                    if(blocks[(int)((y-upB)/size)][(int)((x-leftB)/size)].value!='n'){
                        blocks[(int)((y-upB)/size)][(int)((x-leftB)/size)].clickedR();
                    }
                    break;
                case "brick":
                    if(blocks[(int)((y-upB)/size)][(int)((x-leftB)/size)].value!='b'){
                        blocks[(int)((y-upB)/size)][(int)((x-leftB)/size)].clickedL('b');
                    }
                    break;
                case "light":
                    if(blocks[(int)((y-upB)/size)][(int)((x-leftB)/size)].value!='l'){
                        blocks[(int)((y-upB)/size)][(int)((x-leftB)/size)].clickedL('l');
                    }
            }
        }
    }

    public void processMouseWheel(int x,int y,int v) {
        if((x>=leftBR)&&(x>=leftB)&&(x<=leftBR+sizeR*width)&&(x<=leftB+size*width)&&
                (y>=upBR)&&(y>=upB)&&(y<=upBR+sizeR*height)&&(y<=upB+size*height)){
            if (v == 1) {
                k = 0.91;
            } else {
                k = 1.1;
            }
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    blocks[i][j].wX=x+k*(blocks[i][j].wX-x);
                    blocks[i][j].wY=y+k*(blocks[i][j].wY-y);
                    blocks[i][j].size*=k;

                }
            }
            leftB=x+k*(leftB-x);
            upB=y+k*(upB-y);
            size*=k;
            /*if(leftB>leftBR){
                move((int)(leftBR-leftB),0);
            }
            if (upB > upBR) {
                    move(0,(int)(upBR-upB));
            }*/
        }
    }

    public void processMousePressed(int x, int y){
        lx=x;
        ly=y;
    }

    public void processMouseRelease(){
        lx=-1;
        ly=-1;
    }

    public void processMouseDrag(int x, int y, String brush){
        if(brush.equals("hand")){
            if((x>=leftBR)&&(x>=leftB)&&(x<=leftBR+sizeR*width)&&(x<=leftB+size*width)&&
                    (y>=upBR)&&(y>=upB)&&(y<=upBR+sizeR*height)&&(y<=upB+size*height)){
                if(size>=sizeR){
                    move(x-lx,y-ly);
                    /*if((leftB>leftBR)||(leftB+size*width<leftBR+sizeR*width)||(upB>upBR)||(upB+size*height<upBR+sizeR*height)){
                        move(lx-x,ly-y);
                    }*/
                }else{
                    move(x-lx,y-ly);
                    /*if((leftB<leftBR)||(leftB+size*width>leftBR+sizeR*width)||(upB<upBR)||(upB+size*height>upBR+sizeR*height)){
                        move(lx-x,ly-y);
                    }*/
                }
            }
            lx=x;
            ly=y;
        }
    }

    private void move(int dx, int dy){
            for(int i=0;i<height;i++) {
                for (int j = 0; j < width; j++) {
                    blocks[i][j].wX+=dx;
                    blocks[i][j].wY+=dy;
                }
            }
            leftB+=dx;
            upB+=dy;
    }

}
