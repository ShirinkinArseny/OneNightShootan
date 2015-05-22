package Shootan.Bullets;

import java.util.ArrayList;

public class SmallBullet extends AbstractBullet {


    public SmallBullet(long author, float x, float y, float angle) {
        super(author, x, y, angle, 1, 40f, 100, 0.05f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        return null;
    }
}
