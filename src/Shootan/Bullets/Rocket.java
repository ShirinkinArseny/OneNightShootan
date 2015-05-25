package Shootan.Bullets;

import java.util.ArrayList;

public class Rocket extends Bullet {


    public Rocket() {
        super();
    }

    public Rocket(int author, float x, float y, float angle) {
        super(author, x, y, angle, 20f, 40f, 10f);
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public ArrayList<Bullet> explode() {
        ArrayList<Bullet> bullets=new ArrayList<>(50);
        for (int i=0; i<25; i++)
            bullets.add(new Flame(-1, x, y, (float) (angle+Math.PI*2*i/25)));
        for (int i=0; i<25; i++)
            bullets.add(new BrokenStuff(4, -1, x, y, (float) (angle+Math.PI*2/50+Math.PI*2*i/25)));
        return bullets;
    }
}