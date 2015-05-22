package Shootan.Bullets;

import java.util.ArrayList;

public class Flame extends AbstractBullet {


    public Flame(float x, float y, float angle) {
        super(x, y, angle, 2, 40f, 5, 0.4f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        return null;
    }
}
