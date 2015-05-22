package Shootan.Bullets;

import java.util.ArrayList;

public class Flame extends AbstractBullet {


    public Flame(long author, float x, float y, float angle) {
        super(author, x, y, angle, 2, 10f, 3, 0.4f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        return null;
    }
}
