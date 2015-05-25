package Shootan.Units;

import Shootan.Utils.IndexWrapper;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class UnitSerializationTests {

    @Test
    public void testCreateSerialized() {
        Unit u=new Human(10, 10);


        ArrayList<Byte> serialized=u.fullSerialise();
        Unit k=Unit.createDeserialized(serialized, new IndexWrapper(0));

        assertTrue(Math.abs(k.getX()-u.getX())<=0.01);
        assertTrue(Math.abs(k.getY()-u.getY())<=0.01);
        assertTrue(Math.abs(k.getHealth()-u.getHealth())<=0.01);

        assertEquals(k.currentWeapon, u.currentWeapon);
        assertTrue(Math.abs(k.viewAngle-u.viewAngle)<=0.01);
        assertEquals(k.isMoving, u.isMoving);
        assertTrue(Math.abs(k.motionAngle-u.motionAngle)<=0.01);
        assertEquals(k.wannaShot, u.wannaShot);
    }

    @Test
    public void testFullSerialization() {
        Unit u=new Human(10, 10);

        float x=u.getX();
        float y=u.getY();
        float health=u.getHealth();
        int currentWeapon=u.currentWeapon;
        float viewAngle=u.viewAngle;
        boolean isMoving=u.isMoving;
        float motionAngle=u.motionAngle;
        boolean wannaShot=u.wannaShot;

        ArrayList<Byte> serialized=u.fullSerialise();
        u.fullDeserialise(serialized, new IndexWrapper(0));

        assertTrue(Math.abs(x-u.getX())<=0.01);
        assertTrue(Math.abs(y-u.getY())<=0.01);
        assertTrue(Math.abs(health-u.getHealth())<=0.01);

        assertEquals(currentWeapon, u.currentWeapon);
        assertTrue(Math.abs(viewAngle-u.viewAngle)<=0.01);
        assertEquals(isMoving, u.isMoving);
        assertTrue(Math.abs(motionAngle-u.motionAngle)<=0.01);
        assertEquals(wannaShot, u.wannaShot);
    }

    @Test
    public void testSerializeState() {
        Unit u=new Human(10, 10);

        int currentWeapon=u.currentWeapon;
        float viewAngle=u.viewAngle;
        boolean isMoving=u.isMoving;
        float motionAngle=u.motionAngle;
        boolean wannaShot=u.wannaShot;

        ArrayList<Byte> serialized=u.serializeState();
        u.deserializeState(serialized);

        assertEquals(currentWeapon, u.currentWeapon);
        assertTrue(Math.abs(viewAngle-u.viewAngle)<=0.01);
        assertEquals(isMoving, u.isMoving);
        assertTrue(Math.abs(motionAngle-u.motionAngle)<=0.01);
        assertEquals(wannaShot, u.wannaShot);
    }

}
