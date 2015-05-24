package Shootan.Geometry;

public class Vector {
    private static final float precision = 1.0e-5f;

    private float x,y;
    public float getX() {return x;}
    public float getY() {return y;}
    private float len;
    private void recalculateLen() {len = (float)(Math.sqrt(x*x+y*y));}
    private float angle;
    private void recalculateAngle()  {angle = (float)(Math.atan2(x,y));}
    public float getAngle() {return angle;}

    private Vector() {

    }
    public static Vector createByAngleLength(float len, float angle) {
        Vector v = new Vector();
        v.len = len; v.angle = angle;
        v.x = (float)(Math.cos(angle)*len); v.y = (float)(Math.sin(angle)*len);
        return v;
    }

    public Vector(float x, float y) {
        this.x = x; this.y = y;
        recalculateLen();
        recalculateAngle();
    }
    public Vector(float x1, float y1, float x2, float y2) {
        this(x2-x1,y2-y1);
    }
    public Vector(Vector v) {
        x = v.x; y = v.y; len = v.len; angle = v.angle;
    }


    public boolean isZero() {
        boolean isZero = len<=precision;
        if (isZero) {
            x = 0; y = 0; angle = 0; len = 0;
        }
        return isZero;
    }

    public void normalize() {
        if (!isZero()) {
            x /= len;
            y /= len;
            len = 1;
        }
    }

    public void ortho() {
        float t=x;
        x=y;
        y=t;
    }

    public static Vector add(Vector v1, Vector v2) {
        Vector v = new Vector(v1);
        v.add(v2);
        return v;
    }

    public void add(Vector v) {
        x+=v.x; y+=v.y;
        recalculateLen();
        recalculateAngle();
    }

    public static Vector multiply(Vector v, float a) {
        Vector v1 = new Vector(v);
        v1.multiply(a);
        return v1;
    }

    public void multiply(float a) {
        x*=a; y*=a; len*=Math.abs(a);
    }

    public static float dotProduct(Vector v1, Vector v2) {
        return v1.isZero()||v2.isZero()?0:v1.x*v2.x + v1.y*v2.y;
    }

}
