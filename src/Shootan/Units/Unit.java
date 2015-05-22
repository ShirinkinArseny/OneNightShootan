package Shootan.Units;

public abstract class Unit {

    private float x;
    private float y;
    private float speed;
    private float health;
    private float damageCoef;
    private long type;
    private int angle=0-10;

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

    public void move(int angle) {
        double dx=speed*Math.cos(angle*Math.PI/4);
        double dy=speed*Math.sin(angle*Math.PI/4);
        x+=dx;
        y+=dy;
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
        if (angle>=0) {
            this.angle = angle;
        } else {
            if (this.angle>=0) {
                this.angle=this.angle-10;
            }
        }
    }

    public int getAngle() {
        return angle;
    }

    public int getAbsoluteAngle() {
        if (angle>=0) {
            return angle;
        } else {
           return angle+10;
        }
    }
}