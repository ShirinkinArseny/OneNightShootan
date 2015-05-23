package Shootan.Geometry;

public class Vector {
    private float x,y;
    private float len;
    private boolean isNormalize = false;
    public boolean isNormalized() {return isNormalize;}
    private float angle;
    public float getAngle() {return angle;}
    public Vector(float x, float y) {
        this.x = x; this.y = y;
        len = (float)Math.sqrt(x*x+y*y);
        angle = (float)(Math.atan2(y,x));
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
