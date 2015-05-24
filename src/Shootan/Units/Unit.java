package Shootan.Units;

import Shootan.Weapon.*;

public abstract class Unit {

    private float x;
    private float y;
    private float dx;
    private float dy;
    private float motionAngle;
    private float radiusQuad;
    private float radius;
    private final long id;
    private float speed;
    private float health;
    private float damageCoef;
    private long type;

    private Weapon weapon;

    private boolean isMoving=false;

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving=isMoving;
    }

    public long getId() {
        return id;
    }


    public void setMotionAngle(float motionAngle) {
        while (motionAngle>=Math.PI*2) {
            motionAngle-=Math.PI*2;
        }
        while (motionAngle<0) {
            motionAngle+=Math.PI*2;
        }
        dx= (float) (Math.cos(motionAngle)*speed);
        dy= (float) (Math.sin(motionAngle)*speed);
        this.motionAngle=motionAngle;
    }

    public void setViewAngle(float angle) {
        while (angle>=Math.PI*2) {
            angle-=Math.PI*2;
        }
        while (angle<0) {
            angle+=Math.PI*2;
        }
        viewAngle=angle;
    }

    private float viewAngle;

    public float getMotionAngle() {
        return motionAngle;
    }

    private static long idCounter=0;

    public Unit(float x, float y, float radius, float speed, float damageCoef, long type) {
        this.x=x;
        this.y=y;
        this.radius=radius;
        radiusQuad=radius*radius;
        this.speed=speed;
        this.damageCoef=damageCoef;
        this.type=type;
        this.id=idCounter;
        health=1;
        idCounter++;
        weapon=new RockerLauncher(this);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move(float dt, boolean acceptDx, boolean acceptDy) {
        if (acceptDx) x += dt * dx;
        if (acceptDy) y += dt * dy;
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

    public float getRadius() {return radius;}

    public float getRadiusQuad() {return radiusQuad;}

    public Weapon getWeapon() {
        return weapon;
    }

    public float getViewAngle() {
        return viewAngle;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }
}