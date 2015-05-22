package Shootan.Bullets;

import java.util.ArrayList;

public abstract class AbstractBullet {

    private final float speed;
    private final float damage;
    private final long type;

    protected float x;
    protected float y;
    protected float dx;
    protected float dy;
    protected float hasDistance;
    protected float angle;
    protected long author;

    public AbstractBullet(long author, float x, float y, float angle, long type, float speed, float distance, float damage) {
        this.author = author;
        this.x = x;
        this.y = y;
        this.dx= (float) (Math.cos(angle)*speed);
        this.dy= (float) (Math.sin(angle)*speed);
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

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public float getDX() {
        return dx;
    }

    public float getDY() {
        return dy;
    }
}
