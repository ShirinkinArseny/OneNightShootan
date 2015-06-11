package Shootan.Bullets;

import java.util.ArrayList;

public class Rocket extends Bullet {

    public boolean getIsSecondary() {
        return false;
    }

    public boolean getHasLightning() {
        return true;
    }

    private static final float[] ligtning=new float[]{0.5f, 0.5f, 0.0f};
    public float[] getRGBLigtning() {
        return ligtning;
    }

    public Rocket() {
        super(20f, 40f, 10f);
    }

    public Rocket(int author, float x, float y, float angle) {
        super(author, x, y, angle, 20f, 40f, 10f);
    }


    public static int type;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getName() {
        return "Rocket/Rocket launcher";
    }

    @Override
    public ArrayList<Bullet> explode() {
        ArrayList<Bullet> bullets=new ArrayList<>(45);
        for (int i=0; i<15; i++)
            bullets.add(new LongFlame(author, x, y, (float) (angle+Math.PI*2*i/15)));
        for (int i=0; i<30; i++)
            bullets.add(new BrokenStuff(4, author, x, y, (float) (angle+Math.PI*2/30+Math.PI*2*i/30)));
        return bullets;
    }
}