package Shootan.Bullets;

import Shootan.Utils.IndexWrapper;

import java.util.ArrayList;

import static Shootan.Utils.ByteUtils.*;

public abstract class Bullet {

    private float speed;
    private float damage;

    protected float x;
    protected float y;
    protected float dx;
    protected float dy;
    protected float hasDistance;
    protected float angle;
    protected int author;

    public abstract int getType();

    public ArrayList<Byte> serialize() {
        ArrayList<Byte> data=new ArrayList<>(18);
        data.addAll(uIntToBytes(getType()));
        data.addAll(uIntToBytes(author));
        data.addAll(coordToBytes(hasDistance));
        data.addAll(angleToBytes(angle));
        data.addAll(coordToBytes(x));
        data.addAll(coordToBytes(y));
        return data;
    }

    public void deserialize(ArrayList<Byte> data, IndexWrapper index) {
        author=twoBytesToUInt(data.get(index.value++), data.get(index.value++));
        hasDistance=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
        angle=twoBytesToAngle(data.get(index.value++), data.get(index.value++));
        this.dx= (float) (Math.cos(angle)*speed);
        this.dy= (float) (Math.sin(angle)*speed);
        x=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
        y=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
    }

    public static Bullet createDeserialized(ArrayList<Byte> data, IndexWrapper index) {
        Bullet me=BulletFactory.create(twoBytesToUInt(data.get(index.value++), data.get(index.value++)));
        try {
            me.deserialize(data, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return me;
    }

    public Bullet() {
    }

    public Bullet(int author, float x, float y, float angle, float speed, float distance, float damage) {
        this.author = author;
        this.x = x;
        this.y = y;
        this.dx= (float) (Math.cos(angle)*speed);
        this.dy= (float) (Math.sin(angle)*speed);
        this.angle = angle;
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

    public abstract ArrayList<Bullet> explode();


    public long getAuthor() {
        return author;
    }

    public float getDamage() {
        return damage;
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

    public float getAngle() {
        return angle;
    }
}
