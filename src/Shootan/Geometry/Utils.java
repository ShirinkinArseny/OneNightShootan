package Shootan.Geometry;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Utils {

    public static float getQuadDistFromPointToLine(float lineX1, float lintY1, float lineX2, float lineY2, float pointX, float pointY) {


        float dx=lineX2-lineX1;
        float dy=lineY2-lintY1;
        float a, b, c;
        a=-dy;
        b=dx;
        c=lineX1*dy-lintY1*dx;

        return (float) (Math.pow(a*pointX+b*pointY+c, 2)/(a*a+b*b));
    }

    @Test
    public void testGetQuadDistFromPointToLine() {


        float d=getQuadDistFromPointToLine(0, 0, 10, 0, 5, 5);
        assertTrue(d>=4.9*4.9 && d<=5.1*5.1);

        d=getQuadDistFromPointToLine(0, 1, 1, 0, 1, 1);
        assertTrue(d>=0.45 && d<=0.55);

    }

}
