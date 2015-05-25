package Shootan.Bullets;

import java.util.ArrayList;

public class Flame extends Bullet {


    public Flame() {
        super();
    }

    public Flame(int author, float x, float y, float angle) {
        super(author, x, y, angle, 12f, 1f, 0.1f);
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public ArrayList<Bullet> explode() {
        ArrayList<Bullet> flames=new ArrayList<>(6);
        for (int i=0; i<6; i++)
            flames.add(new SmallFlame(author, x, y, angle+(float) (Math.PI*2*((i-3)/72f))));
        return flames;
    }
}
