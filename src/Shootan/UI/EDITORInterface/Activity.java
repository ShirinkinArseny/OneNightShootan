package Shootan.UI.EDITORInterface;


import java.awt.*;

public class Activity {
    int width,height,x,y,kOfButtons, kOfText;
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

    protected void mouseClick(int x, int y, String brush){
        for(int i=0;i<kOfButtons;i++)
            if(buttons[i].clicked(x,y)) {
                buttons[i].dosmth();
            }
        for(int i=0;i<kOfText;i++) {
            text[i].chosen = text[i].clicked(x, y);
        }
        if(blocksV) {
            //расставляем блоки
            field.mouseClick(x,y,brush);
        }
    }

    protected void keyPressed(char key, int code){
        for(int i=0;i<kOfText;i++){
            if(text[i].chosen){
                if(((key>='a')&&(key<='z'))||((key>='а')&&(key<='я'))||((key>='0')&&(key<='9'))) {
                    text[i].value += key;
                }else {
                    if(code==8) {
                        try {
                            text[i].value = text[i].value.substring(0, text[i].value.length() - 1);
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.println("Все ок, эт не ошибка");
                        }
                    }
                }
            }
        }
    }

    protected void mouseDragged(int x, int y, String brush){
        if(blocksV){
            field.mouseClick(x,y,brush);
        }
    }
}
