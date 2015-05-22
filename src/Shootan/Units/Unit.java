package Shootan.Units;

import Shootan.Blocks.UnitBlock;
import Shootan.Weapon.MP40;
import Shootan.Weapon.RockerLauncher;
import Shootan.Weapon.SniperRifle;
import Shootan.Weapon.Weapon;

public abstract class Unit {

    private float middleX;
    private float middleY;

    public long getId() {
        return id;
    }

    private final long id;
    private UnitBlock block;
    private float speed;
    private float health;
    private float damageCoef;
    private long type;

    private Weapon weapon;

    public boolean isMoving() {
        return block.isMoving();
    }

    public void setIsMoving(boolean isMoving) {
        block.setIsMoving(isMoving);
    }


    public void setMotionAngle(float motionAngle) {
        while (motionAngle>=Math.PI*2) {
            motionAngle-=Math.PI*2;
        }
        while (motionAngle<0) {
            motionAngle+=Math.PI*2;
        }
        block.setAngle(motionAngle);
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
        return block.getAngle();
    }

    private static long idCounter=0;

    public Unit(float x, float y, float radius, float speed, float damageCoef, long type) {
        block = new UnitBlock(x,y,radius);
        this.speed=speed;
        this.damageCoef=damageCoef;
        this.type=type;
        this.id=idCounter;
        idCounter++;
        weapon=new RockerLauncher(this);
    }

    public int getBlockX() {
        return block.getBlockX();
    }

    public int getBlockY() {
        return block.getBlockY();
    }

    public float getX() {
        return block.getX();
    }

    public float getY() {
        return block.getY();
    }

    public UnitBlock tryMove(float dt) {
        return block.moveBlock(dt, speed);
    }

    public void move(float dt) {
        UnitBlock newBlock = block.moveBlock(dt,speed);
        if (newBlock!=null)
            applyMove(newBlock);
    }

    public void applyMove(UnitBlock newBlock) {
        block = newBlock;
    }

    public float getHealth() {
        return health;
    }

    public void damage(float power) {
        health-=power*damageCoef;
        System.out.println("New health: "+health);
    }

    public long getType() {
        return type;
    }

    public float getRadius() {return block.getRadius();}

    public Weapon getWeapon() {
        return weapon;
    }

    public float getViewAngle() {
        return viewAngle;
    }

    public float getMiddleX() {
        return middleX;
    }

    public float getMiddleY() {
        return middleY;
    }
}