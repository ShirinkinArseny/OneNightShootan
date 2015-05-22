package Shootan.Blocks;

public class UnitBlock {
    private static final double minAngle = (Math.PI / 4);


    private float radius;
    public float getRadius() {
        return radius;
    }

    private float x;
    private float y;
    private boolean prevDxPos,prevDyPos;


    //Такое округление неочевидно, но
    //именно оно позволяет не пересекаться с препядствиями
    public int getBlockX() {
        int num = (int) Math.floor(x);
        if (prevDxPos)
            return num+1;
        else
            return num;
    }

    public int getBlockY() {
        int num = (int) Math.floor(y);
        if (prevDyPos)
            return num+1;
        else
            return num;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private boolean isMoving=false;
    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public UnitBlock(float x, float y, float radius) {
        this.x = x; this.y = y; this.radius = radius;
    }
    private UnitBlock(float x, float y, float radius, int angle, boolean prevDxPos, boolean prevDyPos) {
        this(x,y,radius);
        this.prevDxPos = prevDxPos; this.prevDyPos = prevDyPos;
    }

    public UnitBlock moveBlock(float dt, float speed) {
        if (isMoving) {
            double way=speed*dt;
            double dx = way * Math.cos(angle * minAngle);
            double dy = way * Math.sin(angle * minAngle);
            prevDxPos = dx>0; prevDyPos = dy>0;
            return new UnitBlock((float)(x+dx),(float)(y+dy),radius,angle,prevDxPos,prevDyPos);
        }
        return null;
    }
}
