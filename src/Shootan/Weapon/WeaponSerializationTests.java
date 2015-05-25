package Shootan.Weapon;

import Shootan.Units.Human;
import Shootan.Units.Unit;
import Shootan.Utils.IndexWrapper;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class WeaponSerializationTests {

    @Test
    public void testCreateSerialized() {

        Unit owner=new Human(10, 10);

        Weapon w=new RockerLauncher(owner);

        owner.setWannaShot(true);
        w.getNewBullet(0.05f);
        w.getNewBullet(3.19f);

        ArrayList<Byte> data=w.serialize();

        Weapon copy=Weapon.createDeserialized(data, owner, new IndexWrapper(0));
        assertEquals(w.getType(), copy.getType());
        assertEquals(w.getHaveBullets(), copy.getHaveBullets());
        assertEquals(w.getHaveBulletsInCage(), copy.getHaveBulletsInCage());
        assertTrue(Math.abs(w.getTimeToNextShot() - copy.getTimeToNextShot()) < 0.01f);
    }

    @Test
    public void testSerializeState() {
        Weapon w=new RockerLauncher(new Human(10, 10));
        ArrayList<Byte> data=w.serialize();
        int haveBulletsInCage=w.haveBulletsInCage;
        int haveBullets=w.haveBullets;
        float lashShotTime=w.lashShotTime;
        w.deserialize(data, new IndexWrapper(0));
        assertEquals(haveBulletsInCage, w.haveBulletsInCage);
        assertEquals(haveBullets, w.haveBullets);
        assertTrue(Math.abs(lashShotTime-w.lashShotTime)<0.001f);
    }

}
