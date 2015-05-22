package Shootan.Units;

import Shootan.Blocks.UnitBlock;

public abstract class Unit {

    private UnitBlock block;
    private float speed;
    private float health;
    private float damageCoef;
    private long type;
    private float radius;

    public boolean isMoving() {
        return block.isMoving();
    }

    public void setIsMoving(boolean isMoving) {
        block.setIsMoving(isMoving);
    }


    public void setAngle(int angle) {
        block.setAngle(angle);
    }

    public int getAngle() {
        return block.getAngle();
    }


    public Unit(float x, float y, float radius, float speed, float damageCoef, long type) {
        block = new UnitBlock(x,y);
        this.radius=radius;
        this.speed=speed;
        this.damageCoef=damageCoef;
        this.type=type;
    }

    public int getBlockX() {
        return (int) Math.floor(getX());
    }

    public int getBlockY() {
        return (int) Math.floor(getY());
    }

    public float getX() {
        return block.getX();
    }

    public float getY() {
        return block.getY();
    }

    public UnitBlock tryMove(float dt) {
        return block.moveBlock(dt,speed);
    }

    public void move(float dt) {
        UnitBlock newBlock = block.moveBlock(dt,speed);
        if (newBlock!=null)
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

    public float getRadius() {
        return radius;
    }
}