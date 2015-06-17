package Shootan.GameEssences.Bullets;

import java.util.ArrayList;

public class BigBullet extends Bullet {

    public boolean getIsSecondary() {
        return false;
    }

    public boolean getHasLightning() {
        return true;
    }

    private static final float[] ligtning=new float[]{0.3f, 0.3f, 0.0f};
    public float[] getRGBLigtning() {
        return ligtning;
    }

    public BigBullet() {
        super(100f, 1000, 2f);
    }

    public BigBullet(int author, float x, float y, float angle) {
        super(author, x, y, angle, 100f, 1000, 2f);
    }

    public static int type;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getName() {
        return "Big Bullet/Sniper rifle";
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}