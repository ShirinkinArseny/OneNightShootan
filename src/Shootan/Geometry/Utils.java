package Shootan.Geometry;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

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

    public static boolean getQuadIntersectsCircle(int quadX, int quadY,
                                                  float circleX, float circleY,
                                                  float radiusQuad) {

        boolean quad1=radiusQuad>getQuadDistFromPointToLine(quadX, quadY, quadX+1, quadY, circleX, circleY);

        boolean quad2=radiusQuad>getQuadDistFromPointToLine(quadX+1, quadY, quadX+1, quadY+1, circleX, circleY);

        boolean quad3=radiusQuad>getQuadDistFromPointToLine(quadX+1, quadY+1, quadX, quadY+1, circleX, circleY);

        boolean quad4=radiusQuad>getQuadDistFromPointToLine(quadX, quadY+1, quadX, quadY, circleX, circleY);

        return quad1&&quad2 || quad2&&quad3 || quad3&&quad4 || quad4&&quad1;

    }

    @Test
    public void testGetQuadIntersectsCircle() {


        assertTrue(getQuadIntersectsCircle(0, 0, 2, 2, 1.1f));

        assertFalse(getQuadIntersectsCircle(0, 0, 2, 2, 0.9f));

        assertTrue(getQuadIntersectsCircle(1, 1, 0, 0, 1.1f));

        assertFalse(getQuadIntersectsCircle(1, 1, 0, 0, 0.9f));
    }

}
