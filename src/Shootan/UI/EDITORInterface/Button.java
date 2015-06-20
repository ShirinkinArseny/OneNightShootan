package Shootan.UI.EDITORInterface;

public class Button extends Control {

    private Runnable onClickAction=null;

    public Button(int x,int y, String text){
        super(x, y, 100, 20, text);
    }

    public void setAction(Runnable r){
        this.onClickAction=r;
    }

    public void processMousePress(int x, int y) {
        if ((x > this.x) && (x < this.x + width) && (y > this.y) && (y < this.y + height)) {
            if (onClickAction!=null)
                onClickAction.run();
        }
    }

}
