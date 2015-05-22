package Shootan.Geometry;

public class Vector {
    private float x,y;
    private float len;
    private boolean isNormalize = false;
    public boolean isNormalized() {return isNormalize;}
    private float angle;
    public float getAngle() {return angle;}
    private int intAngle;
    public int getIntAngle() {return intAngle;}
    public Vector(float x, float y) {
        this.x = x; this.y = y;
        len = (float)Math.sqrt(x*x+y*y);
        angle = (float)(Math.atan2(x,y)+Math.PI);
        intAngle = (int)Math.floor(angle/2/Math.PI*360);
    }
    public Vector(float x1, float y1, float x2, float y2) {
        this(x2-x1,y2-y1);
    }
    public Vector(Vector v) {
        x = v.x; y = v.y; len = v.len; angle = v.angle;
    }
    public void normalize() {
        x/=len; y/=len;
        isNormalize = true;
    }
}
