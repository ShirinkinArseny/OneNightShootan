package Shootan.Bullets;

import java.util.ArrayList;

public class BrokenStuff extends Bullet {


    public BrokenStuff() {
        super();
    }

    private long pow;

    public BrokenStuff(long pow, int author, float x, float y, float angle) {
        super(author, x, y, angle, 20f, 5, 0.4f);
        this.pow=pow;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public ArrayList<Bullet> explode() {
        if (pow==1) return null;
        ArrayList<Bullet> res=new ArrayList<>(3);
        res.add(new BrokenStuff(pow -1, author, x, y, (float) (angle)));
        res.add(new BrokenStuff(pow -1, author, x, y, (float) (angle+Math.PI*2/3)));
        res.add(new BrokenStuff(pow -1, author, x, y, (float) (angle+Math.PI*2*2/3)));
        return res;
    }
}
