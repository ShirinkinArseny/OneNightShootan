package Shootan.Units;

public abstract class Unit {

    private float x;
    private float y;
    private float speed;
    private float health;
    private float damageCoef;
    private long type;
    private int angle=0;

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    private boolean isMoving=false;

    public Unit(float speed, float damageCoef, long type) {
        this.speed=speed;
        this.damageCoef=damageCoef;
        this.type=type;
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move() {
        if (isMoving) {
            double dx = speed * Math.cos(angle * Math.PI / 4);
            double dy = speed * Math.sin(angle * Math.PI / 4);
            x += dx;
            y += dy;
        }
    }

    public float getHealth() {
        return health;
    }

    public void damage(float power) {
        health-=power*damageCoef;
    }

    public long getType() {
        return type;
    }

    public void setAngle(int angle) {
         this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }
}