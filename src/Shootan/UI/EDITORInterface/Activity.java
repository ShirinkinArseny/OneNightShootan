package Shootan.UI.EDITORInterface;

import org.lwjgl.Sys;

import java.awt.*;

public class Activity {
    int width,height,x,y;
    Button buttons[]=new Button[20];
    TextField text[]=new TextField[10];
    int kOfButtons, kOfText, kOfBlocks=0;
    Block blocks[]=new Block[1024];
    Graphics2D g2;
    boolean blocksV;


    public Activity(int buttons, int text, int x, int y, int width, int height, boolean activeBlocks, Graphics2D g2){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.g2=g2;
        kOfButtons=buttons;
        kOfText=text;
        for(int i=0;i<kOfButtons;i++){
            this.buttons[i]=new Button(20,80+i*20,100,20,i);
        }
        blocksV=activeBlocks;
    }

    protected void draw(){
        for (int i = 0; i < kOfButtons; i++) {
            buttons[i].draw(g2);
        }
        for (int i = 0; i < kOfText; i++) {
            text[i].draw(g2);
        }
        if(blocksV){
            for (int i = 0; i < kOfBlocks; i++) {
                blocks[i].draw(g2);
            }
        }
    }

    protected void mouseClick(int x, int y){
        for(int i=0;i<kOfButtons;i++)
            if(buttons[i].clicked(x,y)) {
                buttons[i].dosmth();
            }
        for(int i=0;i<kOfText;i++) {
            text[i].chosen = text[i].clicked(x, y);
        }
        if((blocksV)&&(x>=200)) {
            boolean cross = false;

            for (int i = 0; i < kOfBlocks; i++) {
                if (!cross) {
                    cross = blocks[i].cross(x, y);
                }
            }
            if (!cross) {
                kOfBlocks += 1;
                blocks[kOfBlocks - 1] = new Block(x, y);
            }
        }
    }

    protected void keyPressed(char key, int code){
        for(int i=0;i<kOfText;i++){
            if(text[i].chosen){
                if(code!=8) {
                    text[i].value += key;
                }else {
                    try{
                        text[i].value=text[i].value.substring(0,text[i].value.length()-1);
                    }catch(StringIndexOutOfBoundsException e){

                    }
                }
            }
        }
    }

    protected void mouseDragged(int x, int y){
        boolean cross=false;
        if((blocksV)&&(x>=200)) {
            for (int i = 0; i < kOfBlocks; i++) {
                if (!cross) {
                    cross = blocks[i].cross(x, y);
                }
            }
            if (!cross) {
                kOfBlocks += 1;
                blocks[kOfBlocks - 1] = new Block(x, y);
            }
        }
    }
}
