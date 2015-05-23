package Shootan.Bullets;

import java.util.ArrayList;

public class Flame extends AbstractBullet {


    public Flame(long author, float x, float y, float angle) {
        super(author, x, y, angle, 2, 10f, 0.5f, 0.4f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        ArrayList<AbstractBullet> flames=new ArrayList<>(6);
        for (int i=0; i<6; i++)
            flames.add(new SmallFlame(author, x, y, angle+(float) (Math.PI*2*((i-3)/72f))));
        return flames;
    }
}
