package Shootan.Bullets;

import java.util.ArrayList;

public abstract class Bullet {

    private final float speed;
    private final float damage;
    private final long type;

    private float x;
    private float y;
    private float angle;

    public Bullet (long type, float speed, float damage) {
        this.type=type;
        this.speed=speed;
        this.damage=damage;
    }

    public void move(float deltaTime) {
        float way=deltaTime*speed;
        x+=way*Math.cos(angle);
        y+=way*Math.sin(angle);
    }

    public abstract ArrayList<Bullet> explode();


}
