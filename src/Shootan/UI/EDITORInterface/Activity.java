package Shootan.UI.EDITORInterface;


import java.awt.*;

public class Activity {
    int width,height,x,y,kOfButtons, kOfText, kOfBlocks=0;
    Button buttons[]=new Button[20];
    TextField text[]=new TextField[10];
    BlockField field;
    Graphics2D g2;
    boolean blocksV;


    public Activity(int buttons, int text, int x, int y, int width, int height, Graphics2D g2){
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
        blocksV=false;
    }

    public Activity(int buttons, int text, int x, int y, int width, int height, int sizeX, int sizeY, Graphics2D g2){
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
        blocksV=true;
        field=new BlockField(sizeX,sizeY,width,height);
    }

    protected void draw(){
        for (int i = 0; i < kOfButtons; i++) {
            buttons[i].draw(g2);
        }
        for (int i = 0; i < kOfText; i++) {
            text[i].draw(g2);
        }
        if(blocksV){
            field.draw(g2);
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
        if(blocksV) {
            //расставляем блоки
            field.mouseClick(x,y);
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
            //И тут расставляем блоки
            field.mouseClick(x,y);
        }
    }
}
