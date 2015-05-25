package Shootan.Bullets;

import java.util.ArrayList;

public class BigBullet extends Bullet {



    public BigBullet() {
        super();
    }

    public BigBullet(int author, float x, float y, float angle) {
        super(author, x, y, angle, 100f, 1000, 2f);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}