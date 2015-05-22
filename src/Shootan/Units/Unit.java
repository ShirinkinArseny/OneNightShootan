package Shootan.Units;

import Shootan.Blocks.UnitBlock;
import Shootan.Geometry.Vector;

public abstract class Unit {

    public long getId() {
        return id;
    }

    private final long id;
    private UnitBlock block;
    private float speed;
    private float health;
    private float damageCoef;
    private long type;

    public boolean isMoving() {
        return block.isMoving();
    }

    public void setIsMoving(boolean isMoving) {
        block.setIsMoving(isMoving);
    }



    private int angle=0;
    public void lookAtPoint(float x, float y) {
        angle = new Vector(block.getX(),block.getY(),x,y).getIntAngle();
    }

    public int getAngle() {
        return angle;
    }

    private static long idCounter=0;


    public Unit(float x, float y, float radius, float speed, float damageCoef, long type) {
        block = new UnitBlock(x,y,radius);
        this.speed=speed;
        this.damageCoef=damageCoef;
        this.type=type;
        this.id=idCounter;
        idCounter++;
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
    }

    public long getType() {
        return type;
    }

    public float getRadius() {return block.getRadius();}
}