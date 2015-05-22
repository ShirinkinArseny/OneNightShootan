package Shootan.Bullets;

import java.util.ArrayList;

public class SmallBullet extends AbstractBullet {


    public SmallBullet(float x, float y, float angle) {
        super(x, y, angle, 1, 40f, 100, 0.05f);
    }

    @Override
    public ArrayList<AbstractBullet> explode() {
        return null;
    }
}
