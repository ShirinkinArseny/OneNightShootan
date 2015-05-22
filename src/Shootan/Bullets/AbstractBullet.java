package Shootan.Bullets;

import java.util.ArrayList;

public abstract class AbstractBullet {

    private final float speed;
    private final float damage;
    private final long type;

    protected float x;
    protected float y;
    private float hasDistance;
    private float angle;
    private long author;

    public AbstractBullet(long author, float x, float y, float angle, long type, float speed, float distance, float damage) {
        this.author = author;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.type=type;
        this.speed=speed;
        this.damage=damage;
        hasDistance=distance;
    }

    public void move(float deltaTime) {
        float way=deltaTime*speed;
        hasDistance-=way;
        x+=way*Math.cos(angle);
        y+=way*Math.sin(angle);
    }

    public boolean hasFallen() {
        return hasDistance<0;
    }

    public abstract ArrayList<AbstractBullet> explode();


    public long getAuthor() {
        return author;
    }

    public float getDamage() {
        return damage;
    }

    public long getType() {
        return type;
    }
}
