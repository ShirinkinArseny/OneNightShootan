package Shootan.Bullets;

import java.util.ArrayList;

public class BrokenStuff extends Bullet {

    public boolean getIsSecondary() {
        return true;
    }

    public boolean getHasLightning() {
        return false;
    }

    public BrokenStuff() {
        super(20f, 5, 0.4f);
    }

    private long pow;

    public BrokenStuff(long pow, int author, float x, float y, float angle) {
        super(author, x, y, angle, 20f, 5, 0.4f);
        this.pow=pow;
    }

    public static int type;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getName() {
        return "Broken stuff/Rocket launcher";
    }

    @Override
    public ArrayList<Bullet> explode() {
        if (pow==1) return null;
        ArrayList<Bullet> res=new ArrayList<>(3);
        res.add(new BrokenStuff(pow -1, author, x, y, (float) (angle)));
        res.add(new BrokenStuff(pow -1, author, x, y, (float) (angle+Math.PI*2/3)));
        res.add(new BrokenStuff(pow -1, author, x, y, (float) (angle+Math.PI*2*2/3)));
        return res;
    }
}
