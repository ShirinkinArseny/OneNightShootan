package Shootan.Bullets;

import java.util.ArrayList;

public class BrokenStuff extends AbstractBullet {


    private long pow;

    public BrokenStuff(long pow, long author, float x, float y, float angle) {
        super(author, x, y, angle, 2, 20f, 5, 0.4f);
        this.pow=pow;
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        if (pow==1) return null;
        ArrayList<AbstractBullet> res=new ArrayList<>(2);
        res.add(new BrokenStuff(pow / 2, author, x, y, (float) (angle+Math.PI*3/2)));
        res.add(new BrokenStuff(pow / 2, author, x, y, (float) (angle+Math.PI/2)));
        return res;
    }
}
