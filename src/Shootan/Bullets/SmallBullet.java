package Shootan.Bullets;

import java.util.ArrayList;

public class SmallBullet extends Bullet {

    public SmallBullet() {
        super();
    }

    public SmallBullet(int author, float x, float y, float angle) {
        super(author, x, y, angle, 40f, 100, 0.05f);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}
