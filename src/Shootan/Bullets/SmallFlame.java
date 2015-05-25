package Shootan.Bullets;

import java.util.ArrayList;

public class SmallFlame extends Bullet {

    public SmallFlame() {
        super();
    }

    public SmallFlame(int author, float x, float y, float angle) {
        super(author, x, y, angle, 12f, 3.5f, 0.05f);
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}
