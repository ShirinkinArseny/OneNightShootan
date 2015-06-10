package Shootan.Bullets;

import java.util.ArrayList;

public class SmallBullet extends Bullet {

    public boolean getIsSecondary() {
        return false;
    }

    public boolean getHasLightning() {
        return true;
    }

    private static final float[] ligtning=new float[]{0.05f, 0.025f, 0.0f};
    public float[] getRGBLigtning() {
        return ligtning;
    }

    public SmallBullet() {
        super(40f, 100, 0.05f);
    }

    public SmallBullet(int author, float x, float y, float angle) {
        super(author, x, y, angle, 40f, 100, 0.05f);
    }


    public static int type;

    @Override
    public int getType() {
        return type;
    }


    @Override
    public String getName() {
        return "Small bullet/MP40";
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}
