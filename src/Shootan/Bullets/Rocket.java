package Shootan.Bullets;

import java.util.ArrayList;

public class Rocket extends AbstractBullet {


    public Rocket(float x, float y, float angle) {
        super(x, y, angle, 0, 7f, 40f, 0.01f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        ArrayList<AbstractBullet> bullets=new ArrayList<>(50);
        for (int i=0; i<50; i++)
            bullets.add(new Flame(x, y, (float) (Math.PI*2*i/50)));
        return bullets;
    }
}