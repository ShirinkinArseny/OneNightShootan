package Shootan.Bullets;

import Shootan.Utils.IndexWrapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class BulletSerializationTests {

    @Test
    public void testCreateSerialized() {
        Bullet b=new Rocket(0, 10, 10, 1.34f);

        ArrayList<Byte> bytes=b.serialize();
        List<Byte> des=bytes.subList(2, bytes.size());
        Bullet l=Bullet.createDeserialized(des, new IndexWrapper(), b.getID());

        assertTrue(Math.abs(b.getX()-l.getX())<=0.01);
        assertTrue(Math.abs(b.getY()-l.getY())<=0.01);

        assertEquals(b.getAuthor(), l.getAuthor());


        assertEquals(b.hasDistance, l.hasDistance);
        assertEquals(b.sourceDistance, l.sourceDistance);

        b.move(0.5f);
        l.move(0.5f);

        assertEquals(b.hasDistance, l.hasDistance);
        assertEquals(b.sourceDistance, l.sourceDistance);

    }

}
