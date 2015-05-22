package Shootan.Bullets;

import java.util.ArrayList;

public class BigBullet extends AbstractBullet {


    public BigBullet(long author, float x, float y, float angle) {
        super(author, x, y, angle, 3, 100f, 1000, 2f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        return null;
    }
}