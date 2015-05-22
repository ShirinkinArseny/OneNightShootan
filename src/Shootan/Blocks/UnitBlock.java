package Shootan.Blocks;

public class UnitBlock {
    private static final double minAngle = (Math.PI / 4);


    private float radius;
    public float getRadius() {
        return radius;
    }

    private float x;
    private float y;

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

    private int angle=0;
    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    private boolean isMoving=false;
    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public UnitBlock(float x, float y, float radius) {
        this.x = x; this.y = y;
    }
    private UnitBlock(float x, float y, float radius, int angle) {
        this(x,y,radius);
        this.angle = angle;
    }

    public UnitBlock moveBlock(float dt, float speed) {
        if (isMoving) {
            double way=speed*dt;
            double dx = way * Math.cos(angle * minAngle);
            double dy = way * Math.sin(angle * minAngle);
            return new UnitBlock((float)(x-dx),(float)(y-dy),angle);
        }
        return null;
    }
}
