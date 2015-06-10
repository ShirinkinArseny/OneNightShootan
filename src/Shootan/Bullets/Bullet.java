package Shootan.Bullets;

import Shootan.Utils.IndexWrapper;

import java.util.ArrayList;
import java.util.List;

import static Shootan.Utils.ByteUtils.*;

public abstract class Bullet {

    private int id;

    private float speed;
    private float damage;

    protected float x;
    protected float y;
    protected float dx;
    protected float dy;
    protected float hasDistance;
    protected float sourceDistance;
    protected float angle;
    protected int author;

    private static int typeRegistrator=0;

    protected static int registerType() {
        typeRegistrator++;
        return typeRegistrator-1;
    }

    static {
        BigBullet.type=registerType();
        BrokenStuff.type=registerType();
        Flame.type=registerType();
        LongFlame.type=registerType();
        Rocket.type=registerType();
        SmallBullet.type=registerType();
        SmallFlame.type=registerType();
    }

    public abstract int getType();

    public abstract String getName();

    public ArrayList<Byte> serialize() {
        ArrayList<Byte> data=new ArrayList<>(24);
        data.addAll(uIntToBytes(id));
        data.addAll(uIntToBytes(getType()));
        data.addAll(uIntToBytes(author));

        float hasDistance=this.hasDistance>0?this.hasDistance:0.001f;
        ArrayList<Byte> hd=coordToBytes(hasDistance);
        data.addAll(hd);
        float hdF=fourBytesToCoord(hd.get(0), hd.get(1), hd.get(2), hd.get(3));
        if (hdF>hasDistance) {
            new Exception("hasDistance>sourceDistance").printStackTrace();
        }


        data.addAll(angleToBytes(angle));
        data.addAll(coordToBytes(x));
        data.addAll(coordToBytes(y));
        return data;
    }

    public void deserialize(List<Byte> data, IndexWrapper index) {
        author=twoBytesToUInt(data.get(index.value++), data.get(index.value++));

        hasDistance=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
        if (hasDistance>sourceDistance) {
            new Exception("hasDistance>sourceDistance").printStackTrace();
        }

        angle=twoBytesToAngle(data.get(index.value++), data.get(index.value++));
        this.dx= (float) (Math.cos(angle)*speed);
        this.dy= (float) (Math.sin(angle)*speed);
        x=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
        y=fourBytesToCoord(data.get(index.value++), data.get(index.value++), data.get(index.value++), data.get(index.value++));
    }

    public static Bullet createDeserialized(List<Byte> data, IndexWrapper index, int id) {
        Bullet me=BulletFactory.create(twoBytesToUInt(data.get(index.value++), data.get(index.value++)));
        try {
            me.deserialize(data, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        me.id=id;
        return me;
    }

    public Bullet(float speed, float distance, float damage) {
        this.speed=speed;
        this.damage=damage;
        this.sourceDistance=distance;
    }

    private static int idRegister=0;

    private static int registerBullet() {
        idRegister++;
        if (idRegister>32000)
            idRegister=0;
        return idRegister;
    }

    public Bullet(int author, float x, float y, float angle, float speed, float distance, float damage) {
        this.id=registerBullet();
        this.author = author;
        this.x = x;
        this.y = y;
        this.dx= (float) (Math.cos(angle)*speed);
        this.dy= (float) (Math.sin(angle)*speed);
        this.angle = angle;
        this.speed=speed;
        this.damage=damage;
        this.sourceDistance=distance;
        hasDistance=distance;
    }

    public void move(float deltaTime) {
        float way=deltaTime*speed;
        hasDistance-=way;
        x+=dx*deltaTime;
        y+=dy*deltaTime;
    }

    public boolean hasFallen() {
        return hasDistance<0;
    }

    public float getRelatedHasDistance() {
        return hasDistance/sourceDistance;
    }

    public abstract ArrayList<Bullet> explode();

    public abstract boolean getHasLightning();

    public float[] getRGBLigtning() {
        return new float[]{0, 0, 0};
    }

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

    public float getSpeed() {
        return speed;
    }

    public abstract boolean getIsSecondary();

    public int getID() {
        return id;
    }
}
