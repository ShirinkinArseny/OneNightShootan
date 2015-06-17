package Shootan.GameEssences.Bullets;

import java.util.ArrayList;

public class SmallFlame extends Bullet {

    public boolean getIsSecondary() {
        return true;
    }

    public boolean getHasLightning() {
        return hasDistance>0;
    }

    public float[] getRGBLigtning() {
        float hasDistance=getRelatedHasDistance();
        return new float[]{hasDistance*0.4f, hasDistance*0.2f, 0};
    }

    public SmallFlame() {
        super(12f, 3.5f, 0.05f);
    }

    public SmallFlame(int author, float x, float y, float angle) {
        super(author, x, y, angle, 12f, 3.5f, 0.05f);
    }

    public static int type;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getName() {
        return "Flame/Flamethrower";
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}
