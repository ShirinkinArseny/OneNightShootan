package Shootan.Bullets;

import java.util.ArrayList;

public class Flame extends Bullet {

    private int explodeSide;

    public boolean getIsSecondary() {
        return false;
    }
    
    public boolean getHasLightning() {
        return true;
    }

    private static final float[] ligtning=new float[]{0.4f, 0.2f, 0.0f};
    public float[] getRGBLigtning() {
        return ligtning;
    }
    public Flame() {
        super(12f, 1f, 0.1f);
    }

    public Flame(int author, float x, float y, float angle, int explodeSide) {
        super(author, x, y, angle, 12f, 1f, 0.1f);
        this.explodeSide = explodeSide;
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
        ArrayList<Bullet> flames=new ArrayList<>(6);
        //for (int i=0; i<6; i++)
            flames.add(new SmallFlame(author, x, y, angle+(float) (Math.PI*2*((explodeSide-3)/72f))));
        return flames;
    }
}
