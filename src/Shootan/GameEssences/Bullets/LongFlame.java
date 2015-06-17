package Shootan.GameEssences.Bullets;

import java.util.ArrayList;

public class LongFlame extends Bullet {

    public boolean getIsSecondary() {
        return true;
    }

    public boolean getHasLightning() {
        return hasDistance>0;
    }

    public float[] getRGBLigtning() {
        float hasDistance=getRelatedHasDistance();
        return new float[]{hasDistance, hasDistance*0.5f, 0f};
    }

    public LongFlame() {
        super(12f, 14f, 0.1f);
    }

    public LongFlame(int author, float x, float y, float angle) {
        super(author, x, y, angle, 12f, 14f, 0.1f);
    }

    public static int type;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getName() {
        return "Flame/Rocket launcher";
    }

    @Override
    public ArrayList<Bullet> explode() {
        return null;
    }
}
