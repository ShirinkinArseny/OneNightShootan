package Shootan.Bullets;

import java.util.ArrayList;

public class SmallFlame extends AbstractBullet {


    public SmallFlame(long author, float x, float y, float angle) {
        super(author, x, y, angle, 2, 10f, 3.5f, 0.1f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        return null;
    }
}
