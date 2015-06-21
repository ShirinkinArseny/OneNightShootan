package Shootan.UI.EDITORInterface;

import java.awt.*;

public class TextField extends Control {

    private static final Color placeHolderText=new Color(192, 192, 192);
    private static final String alphabet="abcdefghijklmnopqrstuvwxyzабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789!@\"#№$;%:^?&**()-_=+[]{};:'\"\\|,<.>/?`~ ";
    private boolean isSelected=false;
    private String placeholder;

    public TextField(int x, int y, String placeholder){
        super(x, y, 100, 20, "");
        this.placeholder = placeholder;
    }

    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (text.length()==0) {
            g2.setColor(placeHolderText);
            g2.drawString(placeholder,x+5,y+height/4*3);
        }
        if (isSelected) {
            g2.setColor(foregroundColor);
            g2.drawRect(x,y,width,height);
        }
    }

    public void processKeyBoardEvent(char c) {
        if (isSelected) {
            if (c==8) {
                if (text.length()>0) {
                    text=text.substring(0, text.length()-1);
                }
            } else
                if (alphabet.contains(String.valueOf(Character.toLowerCase(c)))) {
                    text+=c;
                }
        }
    }

    public void processMousePress(int x, int y, String brush) {
        isSelected=getContainsPoint(x, y);
    }

}
